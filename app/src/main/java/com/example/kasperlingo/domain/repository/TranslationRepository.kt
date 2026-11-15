package com.example.kasperlingo.domain.repository

import com.example.kasperlingo.domain.model.Translation
import kotlinx.coroutines.flow.Flow

interface TranslationRepository {
    fun getAllTranslations(): Flow<List<Translation>>
    fun getFavoriteTranslations(): Flow<List<Translation>>
    suspend fun translateWord(word: String): Result<Translation>
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
    suspend fun deleteTranslation(translation: Translation)
    fun isNetworkAvailable(): Boolean
}
