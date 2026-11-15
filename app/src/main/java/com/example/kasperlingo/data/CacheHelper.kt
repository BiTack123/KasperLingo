package com.example.kasperlingo.data

import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

// управляет кэшированием переводов
@Singleton
class CacheHelper @Inject constructor() {
    
    // проверяет нужно ли обновить кэш (старше 30 минут)
    fun shouldRefreshCache(timestamp: Date): Boolean {
        val currentTime = Date()
        val thirtyMinutesAgo = Date(currentTime.time - 30 * 60 * 1000)
        return timestamp.before(thirtyMinutesAgo)
    }
}
