package com.example.kasperlingo.data.database.dao

import androidx.room.*
import com.example.kasperlingo.data.database.model.TranslationData
import kotlinx.coroutines.flow.Flow

// методы для работы с базой данных
@Dao
interface TranslationDao {
    
    // получаем все переводы, сортируем по времени
    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAllTranslations(): Flow<List<TranslationData>>
    
    // получаем только избранное
    @Query("SELECT * FROM translations WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteTranslations(): Flow<List<TranslationData>>
    
    // ищем перевод по тексту
    @Query("SELECT * FROM translations WHERE originalText = :text LIMIT 1")
    suspend fun getTranslationByText(text: String): TranslationData?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationData): Long
    
    @Update
    suspend fun updateTranslation(translation: TranslationData)
    
    @Delete
    suspend fun deleteTranslation(translation: TranslationData)
    
    @Query("DELETE FROM translations WHERE id = :id")
    suspend fun deleteTranslationById(id: Long)
    
    @Query("UPDATE translations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
}
