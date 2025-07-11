package com.example.chillileafdiseasedetectionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillileafdiseasedetectionapp.data.model.DiseaseStat
import com.example.chillileafdiseasedetectionapp.data.repository.AnalysisResultRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(repository: AnalysisResultRepository) : ViewModel() {

    val stats: StateFlow<List<DiseaseStat>> = repository.diseaseStats
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}