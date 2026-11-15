package com.example.kasperlingo.domain.logic

import javax.inject.Inject
import javax.inject.Singleton

// проверяет правильность введенного слова
@Singleton
class WordChecker @Inject constructor() {
    
    fun checkWord(word: String): ValidationResult {
        val trimmedWord = word.trim()
        
        // проверяем что не пустое
        if (trimmedWord.isEmpty()) {
            return ValidationResult.Error("Введите слово для перевода")
        }
        
        // проверяем что английское
        if (!isEnglishWord(trimmedWord)) {
            return ValidationResult.Error("Введите слово на английском языке")
        }
        
        return ValidationResult.Success(trimmedWord)
    }
    
    // проверяем что в слове только английские буквы
    private fun isEnglishWord(word: String): Boolean {
        val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'-"
        for (char in word) {
            if (char !in allowedChars) {
                return false
            }
        }
        return true
    }
}

sealed class ValidationResult {
    data class Success(val validWord: String) : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
