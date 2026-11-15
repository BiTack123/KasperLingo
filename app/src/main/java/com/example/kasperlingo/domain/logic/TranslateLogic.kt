package com.example.kasperlingo.domain.logic

import com.example.kasperlingo.domain.model.Translation
import com.example.kasperlingo.domain.repository.TranslationRepository
import com.example.kasperlingo.domain.logic.ValidationResult
import com.example.kasperlingo.domain.logic.WordChecker
import javax.inject.Inject

// основная логика перевода слов
class TranslateLogic @Inject constructor(
    private val repository: TranslationRepository,
    private val wordChecker: WordChecker 
) {
    suspend fun execute(word: String): TranslationResult {
        // сначала проверяем слово
        return when (val validation = wordChecker.checkWord(word)) {
            is ValidationResult.Success -> {
                // если слово правильное - переводим
                repository.translateWord(validation.validWord).fold(
                    onSuccess = { TranslationResult.Success(it) },
                    onFailure = { TranslationResult.Error(it.message ?: "Произошла ошибка") }
                )
            }
            is ValidationResult.Error -> {
                TranslationResult.Error(validation.message)
            }
        }
    }
}

sealed class TranslationResult {
    data class Success(val translation: Translation) : TranslationResult()
    data class Error(val message: String) : TranslationResult()
}
