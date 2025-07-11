package com.example.chillileafdiseasedetectionapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chillileafdiseasedetectionapp.ui.NavigationItem
import com.example.chillileafdiseasedetectionapp.ui.theme.ChilliLeafDiseaseDetectionAppTheme
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.AnalyzeViewModel
import java.util.Locale
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: AnalyzeViewModel = viewModel()
) {
    val imageUri by viewModel.imageUri.collectAsStateWithLifecycle()
    val classificationResult by viewModel.classificationResult.collectAsStateWithLifecycle()
    val saveEvent by viewModel.saveEvent.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(saveEvent) {
        if (saveEvent == true) {
            Toast.makeText(context, "Analisis berhasil disimpan!", Toast.LENGTH_SHORT).show()
            navController.popBackStack(NavigationItem.Home.route, inclusive = false)
            viewModel.onSaveEventDone()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Hasil Analisis") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

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
                model = imageUri,
                contentDescription = "Hasil Gambar Analisis",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            classificationResult?.let { (diseaseNameFromModel, accuracy) ->
                val diseaseName = diseaseNameFromModel.trim()
                val accuracyPercent = String.format(Locale.US, "%.1f%%", accuracy * 100)
                val diseaseInfo = getDiseaseDetails(diseaseName)

                ResultCard(disease = diseaseName, accuracy = accuracyPercent)
                InfoCard(title = "Deskripsi Penyakit", content = diseaseInfo.description)
                InfoCard(title = "Rekomendasi Penanganan", content = diseaseInfo.recommendation)
            }

            Button(
                onClick = { viewModel.saveAnalysisResult() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Simpan Hasil", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    ChilliLeafDiseaseDetectionAppTheme {
        ResultScreen(navController = rememberNavController())
    }
}