package com.example.chillileafdiseasedetectionapp.ui

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Analyze : NavigationItem("analyze", Icons.Default.Search, "Analyze")
    object History : NavigationItem("history", Icons.Default.History, "History")
}