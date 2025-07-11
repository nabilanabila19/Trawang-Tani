package com.example.chillileafdiseasedetectionapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult
import com.example.chillileafdiseasedetectionapp.data.model.DiseaseStat
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: AnalysisResult)

    @Query("SELECT * FROM analysis_history ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<AnalysisResult>>

    @Query("SELECT * FROM analysis_history WHERE id = :id")
    fun getResultById(id: String): Flow<AnalysisResult?>

    @Query("DELETE FROM analysis_history WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT disease_name as diseaseName, COUNT(*) as count FROM analysis_history GROUP BY disease_name")
    fun getDiseaseStats(): Flow<List<DiseaseStat>>

    @Query("SELECT * FROM analysis_history WHERE disease_name = :diseaseName ORDER BY timestamp DESC")
    fun getResultsByDisease(diseaseName: String): Flow<List<AnalysisResult>>
}