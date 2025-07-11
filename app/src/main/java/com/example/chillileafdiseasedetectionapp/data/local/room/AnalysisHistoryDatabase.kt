package com.example.chillileafdiseasedetectionapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult

@Database(
    entities = [AnalysisResult::class],
    version = 2,
    exportSchema = false
)
abstract class AnalysisHistoryDatabase : RoomDatabase() {

    abstract fun analysisResultDao(): AnalysisResultDao

    companion object {
        @Volatile
        private var INSTANCE: AnalysisHistoryDatabase? = null

        fun getDatabase(context: Context): AnalysisHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnalysisHistoryDatabase::class.java,
                    "analysis_history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}