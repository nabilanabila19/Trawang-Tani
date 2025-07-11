package com.example.chillileafdiseasedetectionapp.data.repository

import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult
import com.example.chillileafdiseasedetectionapp.data.local.room.AnalysisResultDao
import com.example.chillileafdiseasedetectionapp.data.model.DiseaseStat
import kotlinx.coroutines.flow.Flow

class AnalysisResultRepository(private val analysisResultDao: AnalysisResultDao) {

    val allResults: Flow<List<AnalysisResult>> = analysisResultDao.getAllResults()

    val diseaseStats: Flow<List<DiseaseStat>> = analysisResultDao.getDiseaseStats()

    fun getResultById(id: String): Flow<AnalysisResult?> {
        return analysisResultDao.getResultById(id)
    }

    suspend fun insert(result: AnalysisResult) {
        analysisResultDao.insert(result)
    }

    suspend fun deleteById(id: String) {
        analysisResultDao.deleteById(id)
    }

    fun getResultsByDisease(diseaseName: String): Flow<List<AnalysisResult>> {
        return analysisResultDao.getResultsByDisease(diseaseName)
    }
}