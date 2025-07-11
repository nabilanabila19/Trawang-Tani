package com.example.chillileafdiseasedetectionapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chillileafdiseasedetectionapp.data.repository.AnalysisResultRepository
import com.example.chillileafdiseasedetectionapp.data.repository.UserPreferencesRepository

class ViewModelFactory(
    private val analysisResultRepository: AnalysisResultRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userPreferencesRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(analysisResultRepository) as T
            }
            modelClass.isAssignableFrom(AnalyzeViewModel::class.java) -> {
                AnalyzeViewModel(application, analysisResultRepository) as T
            }
            modelClass.isAssignableFrom(DetailHistoryViewModel::class.java) -> {
                DetailHistoryViewModel(analysisResultRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(analysisResultRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}