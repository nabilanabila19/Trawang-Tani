package com.example.chillileafdiseasedetectionapp.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult
import com.example.chillileafdiseasedetectionapp.data.repository.AnalysisResultRepository
import com.example.chillileafdiseasedetectionapp.helper.ImageClassifierHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.util.UUID
import kotlinx.coroutines.delay

class AnalyzeViewModel(application: Application, private val repository: AnalysisResultRepository) : AndroidViewModel(application), ImageClassifierHelper.ClassifierListener {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    private val _classificationResult = MutableStateFlow<Pair<String, Float>?>(null)
    val classificationResult = _classificationResult.asStateFlow()

    private val _navigateToResult = MutableStateFlow(false)
    val navigateToResult = _navigateToResult.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _saveEvent = MutableStateFlow<Boolean?>(null)
    val saveEvent = _saveEvent.asStateFlow()

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    init {
        imageClassifierHelper = ImageClassifierHelper(
            context = getApplication<Application>().applicationContext,
            classifierListener = this
        )
    }

    fun onImageSelected(uri: Uri?) {
        _imageUri.update { uri }
    }

    fun analyzeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _classificationResult.value = null
            delay(2000L)
            imageClassifierHelper.classifyImage(bitmap)
        }
    }


    override fun onError(error: String) {
        _error.update { error }
        _isAnalyzing.value = false
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        results?.let {
            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                val topResult = it[0].categories[0]
                _classificationResult.update { Pair(topResult.label, topResult.score) }
                _navigateToResult.value = true
            } else {
                _classificationResult.update { null }
                _error.update { "Tidak dapat mengklasifikasikan gambar." }
            }
        }
        _isAnalyzing.value = false
    }

    fun saveAnalysisResult() {
        val currentImageUri = _imageUri.value
        val currentResult = _classificationResult.value

        if (currentImageUri != null && currentResult != null) {
            val permanentImageUri = ImageClassifierHelper.saveImageToInternalStorage(
                getApplication<Application>().applicationContext,
                currentImageUri
            )

            if (permanentImageUri != null) {
                val (diseaseName, accuracy) = currentResult

                val analysisResult = AnalysisResult(
                    id = UUID.randomUUID().toString(),
                    imageUri = permanentImageUri.toString(),
                    diseaseName = diseaseName,
                    accuracy = accuracy,
                    timestamp = System.currentTimeMillis(),
                )

                viewModelScope.launch {
                    repository.insert(analysisResult)
                    _saveEvent.value = true
                }
            } else {
                onError("Gagal menyimpan gambar.")
            }
        }
    }

    fun onSaveEventDone() {
        _saveEvent.value = null
    }

    fun onNavigationToResultDone() {
        _navigateToResult.value = false
    }
}