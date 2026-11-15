package com.example.kasperlingo.presentation.viewmodel

import com.example.kasperlingo.domain.model.Translation

// состояние экрана избранного
data class FavoritesState(
    val favoriteTranslations: List<Translation> = emptyList(), // список избранных
    val isLoading: Boolean = false,
    val error: String? = null
) {
    // нет ли избранных переводов
    fun hasNoFavorites(): Boolean {
        return favoriteTranslations.isEmpty()
    }
    
    // количество избранных переводов
    fun getCount(): Int {
        return favoriteTranslations.size
    }
}
