package com.example.kasperlingo.injection

import com.example.kasperlingo.data.repository.TranslationRepo
import com.example.kasperlingo.domain.repository.TranslationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    
    @Binds
    @Singleton
    abstract fun bindTranslationRepository(
        translationRepository: TranslationRepo
    ): TranslationRepository
}
