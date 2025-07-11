package com.example.chillileafdiseasedetectionapp

import android.app.Application
import com.example.chillileafdiseasedetectionapp.data.local.room.AnalysisHistoryDatabase
import com.example.chillileafdiseasedetectionapp.data.repository.AnalysisResultRepository
import com.example.chillileafdiseasedetectionapp.data.repository.UserPreferencesRepository

class ChilliApplication : Application() {
    val database by lazy { AnalysisHistoryDatabase.getDatabase(this) }

    val analysisResultRepository by lazy { AnalysisResultRepository(database.analysisResultDao()) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(this) }
}