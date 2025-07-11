package com.example.chillileafdiseasedetectionapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult
import com.example.chillileafdiseasedetectionapp.data.repository.AnalysisResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailHistoryViewModel(private val repository: AnalysisResultRepository) : ViewModel() {

    private val _result = MutableStateFlow<AnalysisResult?>(null)
    val result = _result.asStateFlow()

    fun getResultById(id: String) {
        viewModelScope.launch {
            repository.getResultById(id).collect {
                _result.value = it
            }
        }
    }
}