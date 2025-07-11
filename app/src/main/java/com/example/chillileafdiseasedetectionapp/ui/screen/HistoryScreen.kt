package com.example.chillileafdiseasedetectionapp.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.FolderOff
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chillileafdiseasedetectionapp.data.model.AnalysisResult
import com.example.chillileafdiseasedetectionapp.helper.DateHelper
import com.example.chillileafdiseasedetectionapp.ui.theme.ChilliLeafDiseaseDetectionAppTheme
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    navController: NavController
) {
    val historyList by viewModel.historyState.collectAsState()
    val uniqueDiseases by viewModel.uniqueDiseaseNames.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()

    /*LaunchedEffect(initialFilter) {
        viewModel.setFilter(initialFilter ?: "Semua")
    }*/

    HistoryScreenContent(
        historyList = historyList,
        uniqueDiseases = uniqueDiseases,
        activeFilter = activeFilter,
        onFilterClick = { diseaseName ->
            viewModel.setFilter(diseaseName)
        },
        onItemClick = { resultId ->
            navController.navigate("history/detail/$resultId")
        },
        onItemDeleteRequest = { result ->
            viewModel.deleteResult(result.id)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreenContent(
    historyList: List<AnalysisResult>,
    uniqueDiseases: List<String>,
    activeFilter: String,
    onFilterClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onItemDeleteRequest: (AnalysisResult) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<AnalysisResult?>(null) }
    val context = LocalContext.current

    if (showDialog) {
        CustomDeleteDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = {
                itemToDelete?.let { onItemDeleteRequest(it) }
                showDialog = false
                Toast.makeText(context, "Riwayat Analisis berhasil dihapus", Toast.LENGTH_SHORT).show()
            },
            diseaseName = itemToDelete?.diseaseName ?: ""
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Riwayat Analisis") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uniqueDiseases) { disease ->
                    FilterChip(
                        selected = disease == activeFilter,
                        onClick = { onFilterClick(disease) },
                        label = { Text(disease) },
                        leadingIcon = if (disease == activeFilter) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Filter aktif",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            if (historyList.isEmpty()) {
                EmptyHistoryView()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historyList, key = { it.id }) { result ->
                        HistoryItemCard(
                            result = result,
                            onClick = { onItemClick(result.id) },
                            onLongClick = {
                                itemToDelete = result
                                showDialog = true
                            },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryItemCard(
    result: AnalysisResult,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accuracyColor = when {
        result.accuracy * 100 >= 85 -> MaterialTheme.colorScheme.primary
        result.accuracy * 100 >= 70 -> Color(0xFFD4A21D)
        else -> Color(0xFFC44536)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = Uri.parse(result.imageUri),
                contentDescription = "Gambar: ${result.diseaseName}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.diseaseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Akurasi: ${String.format("%.1f%%", result.accuracy * 100)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = accuracyColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = DateHelper.formatTimestamp(result.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Lihat Detail",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.FolderOff,
                contentDescription = "Riwayat Kosong",
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Riwayat Analisis Kosong",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hasil analisis yang Anda simpan akan muncul di sini.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CustomDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    diseaseName: String
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismissRequest,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "Ikon Peringatan",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Konfirmasi Hapus",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Apakah Anda yakin ingin menghapus riwayat analisis '$diseaseName'? Aksi ini tidak dapat dibatalkan.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Hapus", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Batal", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Preview(showBackground = true, name = "History Item Card")
@Composable
fun HistoryItemCardPreview() {
    ChilliLeafDiseaseDetectionAppTheme {
        val dummyResult = AnalysisResult("1", "", "Bacterial Spot", 0.957f, 0)
        HistoryItemCard(result = dummyResult, onClick = {}, onLongClick = {})
    }
}

@Preview(showBackground = true, name = "History Screen - Populated")
@Composable
fun HistoryScreenPopulatedPreview() {
    ChilliLeafDiseaseDetectionAppTheme {
        val dummyList = listOf(
            AnalysisResult("1", "", "Bacterial Spot", 0.957f, 0),
            AnalysisResult("2", "", "Daun Keriting", 0.991f, 0),
        )
        val dummyFilters = listOf("Semua", "Bacterial Spot", "Daun Keriting")

        HistoryScreenContent(
            historyList = dummyList,
            uniqueDiseases = dummyFilters,
            activeFilter = "Bacterial Spot",
            onFilterClick = {},
            onItemClick = {},
            onItemDeleteRequest = {}
        )
    }
}

@Preview(showBackground = true, name = "History Screen - Empty")
@Composable
fun HistoryScreenEmptyPreview() {
    ChilliLeafDiseaseDetectionAppTheme {
        HistoryScreenContent(
            historyList = emptyList(),
            uniqueDiseases = listOf("Semua"),
            activeFilter = "Semua",
            onFilterClick = {},
            onItemClick = {},
            onItemDeleteRequest = {}
        )
    }
}