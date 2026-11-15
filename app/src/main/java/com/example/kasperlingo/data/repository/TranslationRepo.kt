package com.example.kasperlingo.data.repository

import com.example.kasperlingo.data.api.SkyengApi
import com.example.kasperlingo.data.CacheHelper
import com.example.kasperlingo.data.database.dao.TranslationDao
import com.example.kasperlingo.data.utils.TranslationMapper
import com.example.kasperlingo.data.NetworkHelper
import com.example.kasperlingo.domain.error.AppError
import com.example.kasperlingo.domain.error.ErrorUtils
import com.example.kasperlingo.domain.model.Translation
import com.example.kasperlingo.domain.repository.TranslationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Основная логика работы с переводами
@Singleton
class TranslationRepo @Inject constructor(
    private val api: SkyengApi, 
    private val dao: TranslationDao,
    private val mapper: TranslationMapper,
    private val errorUtils: ErrorUtils,
    private val networkHelper: NetworkHelper,
    private val cacheHelper: CacheHelper 
) : TranslationRepository {
    
    override fun getAllTranslations(): Flow<List<Translation>> = 
        dao.getAllTranslations().map { entities ->
            mapper.entityListToDomain(entities)
        }
    
    override fun getFavoriteTranslations(): Flow<List<Translation>> = 
        dao.getFavoriteTranslations().map { entities ->
            mapper.entityListToDomain(entities)
        }
    
    override fun isNetworkAvailable(): Boolean = networkHelper.isNetworkAvailable()
    
    // переводит слово через API или берет из кэша
    override suspend fun translateWord(word: String): Result<Translation> {
        return try {
            // сначала ищем в базе
            val cachedTranslation = dao.getTranslationByText(word)
            
            // если есть и не устарел - возвращаем
            if (cachedTranslation != null && !cacheHelper.shouldRefreshCache(cachedTranslation.timestamp)) {
                return Result.success(mapper.entityToDomain(cachedTranslation))
            }
            
            // проверяем интернет
            if (!networkHelper.isNetworkAvailable()) {
                if (cachedTranslation != null) {
                    return Result.success(mapper.entityToDomain(cachedTranslation))
                } else {
                    val error = AppError.NetworkError("Нет подключения к интернету")
                    return Result.failure(Exception(error.message))
                }
            }
            
            // делаем запрос к API с повторами
            val response = performNetworkCallWithRetry(word)
            
            if (response.isNotEmpty()) {
                // сохраняем перевод в базу
                val translationEntity = mapper.dtoToEntity(word, response.first())
                val id = dao.insertTranslation(translationEntity)
                val updatedEntity = translationEntity.copy(id = id)
                Result.success(mapper.entityToDomain(updatedEntity))
            } else {
                val error = AppError.NotFoundError("Перевод не найден")
                Result.failure(Exception(error.message))
            }
        } catch (e: Exception) {
            val appError = errorUtils.handleError(e)
            Result.failure(Exception(appError.message))
        }
    }
    
    private suspend fun performNetworkCallWithRetry(word: String, retries: Int = 3): List<com.example.kasperlingo.data.response.SkyengResponse> {
        repeat(retries) { attempt ->
            try {
                return api.searchWords(word)
            } catch (e: Exception) {
                if (attempt == retries - 1) {
                    throw e
                }
                delay(1000L * (attempt + 1))
            }
        }
        return emptyList()
    }
    
    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        dao.updateFavoriteStatus(id, isFavorite)
    }
    
    override suspend fun deleteTranslation(translation: Translation) {
        val entity = mapper.domainToEntity(translation)
        dao.deleteTranslation(entity)
    }
}
