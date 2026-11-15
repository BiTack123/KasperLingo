package com.example.kasperlingo.data.utils

import com.example.kasperlingo.data.database.model.TranslationData
import com.example.kasperlingo.data.response.SkyengResponse
import com.example.kasperlingo.domain.model.Translation
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

// преобразует данные между разными форматами
@Singleton
class TranslationMapper @Inject constructor() {
    
    // из API ответа в базу данных
    fun dtoToEntity(
        originalText: String,
        dto: SkyengResponse
    ): TranslationData {
        // если API не вернуло переводов
        if (dto.meanings.isEmpty()) {
            return TranslationData(
                originalText = originalText,
                translatedText = "Перевод не найден",
                transcription = null,
                imageUrl = null,
                timestamp = Date()
            )
        }
        
        val meaning = dto.meanings.first()
        val translatedText = meaning.translation.text
        
        return TranslationData(
            originalText = originalText,
            translatedText = translatedText,
            transcription = meaning.transcription,
            imageUrl = meaning.imageUrl,
            timestamp = Date()
        )
    }
    
    fun entityToDomain(entity: TranslationData): Translation {
        return Translation(
            id = entity.id,
            originalText = entity.originalText,
            translatedText = entity.translatedText,
            transcription = entity.transcription,
            imageUrl = entity.imageUrl,
            isFavorite = entity.isFavorite,
            timestamp = entity.timestamp
        )
    }
    
    fun domainToEntity(domain: Translation): TranslationData {
        return TranslationData(
            id = domain.id,
            originalText = domain.originalText,
            translatedText = domain.translatedText,
            transcription = domain.transcription,
            imageUrl = domain.imageUrl,
            isFavorite = domain.isFavorite,
            timestamp = domain.timestamp
        )
    }
    
    fun entityListToDomain(entities: List<TranslationData>): List<Translation> {
        val translations = mutableListOf<Translation>()
        for (entity in entities) {
            translations.add(entityToDomain(entity))
        }
        return translations
    }
}
