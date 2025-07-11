package com.example.chillileafdiseasedetectionapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_history")
data class AnalysisResult(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "image_uri")
    val imageUri: String,

    @ColumnInfo(name = "disease_name")
    val diseaseName: String,

    @ColumnInfo(name = "accuracy")
    val accuracy: Float,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)