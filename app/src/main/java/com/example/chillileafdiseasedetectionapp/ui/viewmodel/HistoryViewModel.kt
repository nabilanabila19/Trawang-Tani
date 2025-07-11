package com.example.chillileafdiseasedetectionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult
import com.example.chillileafdiseasedetectionapp.data.repository.AnalysisResultRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(private val repository: AnalysisResultRepository) : ViewModel() {

    private val _activeFilter = MutableStateFlow("Semua")
    val activeFilter = _activeFilter.asStateFlow()

    val historyState: StateFlow<List<AnalysisResult>> = _activeFilter.flatMapLatest { filter ->
        if (filter == "Semua") {
            repository.allResults
        } else {
            repository.getResultsByDisease(filter)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val uniqueDiseaseNames: StateFlow<List<String>> = repository.allResults.map { results ->
        listOf("Semua") + results.map { it.diseaseName }.distinct()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = listOf("Semua")
    )

    fun setFilter(diseaseName: String) {
        _activeFilter.value = diseaseName
    }

    fun deleteResult(id: String) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}