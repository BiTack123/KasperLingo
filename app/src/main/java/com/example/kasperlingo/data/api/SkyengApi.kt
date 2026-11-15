package com.example.kasperlingo.data.api

import com.example.kasperlingo.data.response.SkyengResponse
import retrofit2.http.GET
import retrofit2.http.Query

// API для работы с переводчиком Skyeng
interface SkyengApi {
    
    // ищем переводы слова
    @GET("words/search")
    suspend fun searchWords(
        @Query("search") query: String
    ): List<SkyengResponse>
    
    companion object {
        // адрес API
        const val BASE_URL = "https://dictionary.skyeng.ru/api/public/v1/"
    }
}
