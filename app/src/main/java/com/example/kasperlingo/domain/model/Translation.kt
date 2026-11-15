package com.example.kasperlingo.domain.model

import java.util.Date

// модель перевода для UI
data class Translation(
    val id: Long = 0,
    val originalText: String,
    val translatedText: String,
    val transcription: String? = null,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val timestamp: Date = Date()
)
