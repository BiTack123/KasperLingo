package com.example.kasperlingo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kasperlingo.data.database.DateUtils
import com.example.kasperlingo.data.database.dao.TranslationDao
import com.example.kasperlingo.data.database.model.TranslationData

@Database(
    entities = [TranslationData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateUtils::class)
abstract class TranslatorDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}
