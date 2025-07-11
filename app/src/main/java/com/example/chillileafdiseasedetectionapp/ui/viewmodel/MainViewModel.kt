package com.example.chillileafdiseasedetectionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillileafdiseasedetectionapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = true,
    val hasCompletedOnboarding: Boolean = false
)

class MainViewModel(private val repository: UserPreferencesRepository) : ViewModel() {

    val uiState: StateFlow<MainUiState> = repository.onboardingCompletedFlow
        .map { isCompleted ->
            MainUiState(isLoading = false, hasCompletedOnboarding = isCompleted)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState(isLoading = true, hasCompletedOnboarding = false)
        )

    fun setOnboardingCompleted() {
        viewModelScope.launch {
            repository.setOnboardingCompleted(true)
        }
    }
}