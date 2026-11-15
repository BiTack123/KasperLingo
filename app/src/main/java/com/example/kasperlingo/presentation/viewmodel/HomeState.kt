package com.example.kasperlingo.presentation.viewmodel

import com.example.kasperlingo.domain.model.Translation

// состояние главного экрана
data class HomeState(
    val searchQuery: String = "", 
    val translations: List<Translation> = emptyList(),
    val isLoading: Boolean = false, 
    val error: String? = null, 
    val isNetworkAvailable: Boolean = true 
) {
    fun hasData(): Boolean {
        return translations.isNotEmpty()
    }
    
    // показывать ли пустой экран
    fun isEmpty(): Boolean {
        return !isLoading && translations.isEmpty() && error == null
    }
}
