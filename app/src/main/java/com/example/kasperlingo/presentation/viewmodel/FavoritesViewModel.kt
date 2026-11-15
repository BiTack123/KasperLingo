package com.example.kasperlingo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasperlingo.domain.model.Translation
import com.example.kasperlingo.domain.repository.TranslationRepository
import com.example.kasperlingo.presentation.viewmodel.FavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel для экрана избранного
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: TranslationRepository
) : ViewModel() {
    
    // состояние экрана избранного
    private val _uiState = MutableStateFlow(FavoritesState())
    val uiState: StateFlow<FavoritesState> = _uiState.asStateFlow()
    
    init {
        observeFavoriteTranslations() // подписываемся на избранные переводы
    }
    
    private fun observeFavoriteTranslations() {
        viewModelScope.launch {
            repository.getFavoriteTranslations().collect { translations ->
                _uiState.value = _uiState.value.copy(
                    favoriteTranslations = translations
                )
            }
        }
    }
    
    fun toggleFavorite(translation: Translation) {
        viewModelScope.launch {
            repository.toggleFavorite(translation.id, !translation.isFavorite)
        }
    }
    
    fun deleteTranslation(translation: Translation) {
        viewModelScope.launch {
            repository.deleteTranslation(translation)
        }
    }
}
