package com.example.kasperlingo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasperlingo.domain.model.Translation
import com.example.kasperlingo.domain.repository.TranslationRepository
import com.example.kasperlingo.domain.logic.TranslateLogic
import com.example.kasperlingo.domain.logic.TranslationResult
import com.example.kasperlingo.presentation.viewmodel.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel для главного экрана
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TranslationRepository,
    private val translateLogic: TranslateLogic
) : ViewModel() {
    
    // состояние UI
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()
    
    init {
        observeTranslations()
        checkNetworkStatus()
    }
    
    private fun observeTranslations() {
        viewModelScope.launch {
            repository.getAllTranslations().collect { translations ->
                _uiState.value = _uiState.value.copy(
                    translations = translations
                )
            }
        }
    }
    
    private fun checkNetworkStatus() {
        _uiState.value = _uiState.value.copy(
            isNetworkAvailable = repository.isNetworkAvailable()
        )
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            error = null
        )
    }
    
    // главная функция - переводит слово
    fun translateWord() {
        val query = _uiState.value.searchQuery
        
        viewModelScope.launch {
            // показываем загрузку
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            checkNetworkStatus()
            
            // пытаемся перевести
            when (val result = translateLogic.execute(query)) {
                is TranslationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        searchQuery = "",
                        isLoading = false,
                        error = null
                    )
                }
                is TranslationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
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
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
