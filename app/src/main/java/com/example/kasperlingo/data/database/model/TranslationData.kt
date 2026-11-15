package com.example.kasperlingo.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// модель для хранения переводов в базе
@Entity(tableName = "translations")
data class TranslationData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originalText: String,
    val translatedText: String,
    val transcription: String? = null,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false, 
    val timestamp: Date = Date()
)
