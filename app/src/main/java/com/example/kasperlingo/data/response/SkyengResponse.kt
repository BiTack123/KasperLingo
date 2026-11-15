package com.example.kasperlingo.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SkyengResponse(
    @SerialName("id") val id: Int,
    @SerialName("text") val text: String,
    @SerialName("meanings") val meanings: List<Meaning>
)

@Serializable
data class Meaning(
    @SerialName("id") val id: Int,
    @SerialName("partOfSpeechCode") val partOfSpeechCode: String? = null,
    @SerialName("translation") val translation: TranslationText,
    @SerialName("previewUrl") val previewUrl: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("transcription") val transcription: String? = null,
    @SerialName("soundUrl") val soundUrl: String? = null
)

@Serializable
data class TranslationText(
    @SerialName("text") val text: String,
    @SerialName("note") val note: String? = null
)
