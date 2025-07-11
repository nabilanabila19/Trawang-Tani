package com.example.chillileafdiseasedetectionapp.ui.screen

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chillileafdiseasedetectionapp.helper.DateHelper
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.DetailHistoryViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.ViewModelFactory
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailHistoryScreen(
    resultId: String,
    navController: NavController,
    factory: ViewModelFactory
) {
    val viewModel: DetailHistoryViewModel = viewModel(factory = factory)
    val result by viewModel.result.collectAsState()

    LaunchedEffect(key1 = resultId) {
        viewModel.getResultById(resultId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detail Riwayat") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        result?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = Uri.parse(data.imageUri),
                    contentDescription = "Gambar: ${data.diseaseName}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                val diseaseName = data.diseaseName.trim()

                val accuracyPercent = String.format(Locale.US, "%.1f%%", data.accuracy * 100)
                val diseaseInfo = getDiseaseDetails(diseaseName)

                ResultCard(disease = data.diseaseName, accuracy = accuracyPercent)
                InfoCard(title = "Deskripsi Penyakit", content = diseaseInfo.description)
                InfoCard(title = "Rekomendasi Penanganan", content = diseaseInfo.recommendation)

                InfoCard(
                    title = "Detail Analisis",
                    content = "Disimpan pada: ${DateHelper.formatTimestamp(data.timestamp)}"
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (result == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}