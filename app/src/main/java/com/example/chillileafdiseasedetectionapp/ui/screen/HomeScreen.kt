package com.example.chillileafdiseasedetectionapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.chillileafdiseasedetectionapp.data.model.DiseaseStat
import com.example.chillileafdiseasedetectionapp.ui.NavigationItem
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.HistoryViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.HomeViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.ViewModelFactory
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    factory: ViewModelFactory,
    historyViewModel: HistoryViewModel
) {
    val stats by viewModel.stats.collectAsState()
    HomeScreenContent(navController = navController, stats = stats, historyViewModel = historyViewModel)
}

@Composable
fun HomeScreenContent(navController: NavController, stats: List<DiseaseStat>, historyViewModel: HistoryViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            HeaderSection(
                navController = navController,
                userName = "Nabila"
            )
        }
        item {
            StatistikSection(
                stats = stats,
                modifier = Modifier.padding(horizontal = 16.dp),
                navController = navController,
                historyViewModel = historyViewModel
            )
        }
        item {
            PintasanSection(
                navController = navController,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun HeaderSection(navController: NavController, userName: String) {
    val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentTime) {
        in 0..10 -> "Selamat Pagi"
        in 11..14 -> "Selamat Siang"
        in 15..18 -> "Selamat Sore"
        else -> "Selamat Malam"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "$greeting, $userName!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Ayo cek kesehatan tanamanmu!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        FilledTonalIconButton(
            onClick = { navController.navigate("about") }
//            modifier = Modifier
//                .clip(CircleShape)
//                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Info Aplikasi",
//                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatistikSection(
    stats: List<DiseaseStat>,
    modifier: Modifier = Modifier,
    navController: NavController,
    historyViewModel: HistoryViewModel
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Statistik Penyakit",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (stats.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Belum ada data riwayat untuk ditampilkan. Mulai analisis pertama Anda!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .fillMaxWidth()
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stats, key = { it.diseaseName }) { stat ->
                    StatCardModern(
                        stat = stat,
                        modifier = Modifier.fillParentMaxWidth(0.45f),
                        onClick = {
                            historyViewModel.setFilter(stat.diseaseName)

                            navController.navigate(NavigationItem.History.route) {
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatCardModern(stat: DiseaseStat, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val icon = when {
        stat.diseaseName.contains("Cercospora", ignoreCase = true) -> Icons.Default.BubbleChart
        stat.diseaseName.contains("Nutrition", ignoreCase = true) -> Icons.Default.Science
        stat.diseaseName.contains("Bacterial", ignoreCase = true) -> Icons.Default.Grain
        stat.diseaseName.contains("Curl", ignoreCase = true) -> Icons.Default.FilterVintage
        stat.diseaseName.contains("Healthy", ignoreCase = true) -> Icons.Default.Verified
        stat.diseaseName.contains("White", ignoreCase = true) -> Icons.Default.Flare
        else -> Icons.Default.Spa
    }
    val cardColor = MaterialTheme.colorScheme.secondaryContainer
    val onCardColor = MaterialTheme.colorScheme.onSecondaryContainer

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stat.diseaseName,
                modifier = Modifier.size(36.dp),
                tint = onCardColor.copy(alpha = 0.8f)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stat.diseaseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onCardColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "${stat.count} kasus",
                style = MaterialTheme.typography.bodyLarge,
                color = onCardColor.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun PintasanSection(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Pintasan Cepat",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        ShortcutCardModern(
            title = "Deteksi Penyakit",
            subtitle = "Ambil atau unggah gambar daun cabai",
            icon = Icons.Outlined.Analytics,
            onClick = {
                navController.navigate(NavigationItem.Analyze.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
        ShortcutCardModern(
            title = "Riwayat Analisis",
            subtitle = "Lihat kembali hasil deteksi Anda",
            icon = Icons.Outlined.History,
            onClick = {
                navController.navigate(NavigationItem.History.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
fun ShortcutCardModern(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = contentColor
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.85f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}