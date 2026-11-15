package com.example.kasperlingo.injection

import android.content.Context
import androidx.room.Room
import com.example.kasperlingo.data.database.TranslatorDatabase
import com.example.kasperlingo.data.database.dao.TranslationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// настройки для базы данных
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    // создаем базу данных
    @Provides
    @Singleton
    fun provideTranslatorDatabase(
        @ApplicationContext context: Context
    ): TranslatorDatabase {
        return Room.databaseBuilder(
            context,
            TranslatorDatabase::class.java,
            "translator_database" // имя файла базы
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideTranslationDao(database: TranslatorDatabase): TranslationDao {
        return database.translationDao()
    }
}
