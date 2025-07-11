package com.example.chillileafdiseasedetectionapp.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chillileafdiseasedetectionapp.R
import com.example.chillileafdiseasedetectionapp.ui.theme.ChilliLeafDiseaseDetectionAppTheme
import com.google.accompanist.pager.*

data class OnboardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        image = R.drawable.onboarding_1_no_bg,
        title = "Selamat Datang di Trawang Tani",
        description = "Asisten cerdas untuk memindai dan mendeteksi penyakit tanaman pada daun cabai Anda secara instan."
    ),
    OnboardingPage(
        image = R.drawable.onboarding_2,
        title = "Deteksi dengan Kamera",
        description = "Ambil atau unggah gambar daun, dan sistem AI akan menganalisisnya untuk Anda."
    ),
    OnboardingPage(
        image = R.drawable.onboarding_3,
        title = "Pantau Riwayat Kesehatan",
        description = "Simpan hasil analisis dan pantau riwayat kesehatan kebun cabai Anda dari waktu ke waktu."
    )
)

@Composable
fun OnboardingScreen(
    onOnboardingFinished: () -> Unit
) {
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = onboardingPages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            OnboardingPageContent(page = onboardingPages[pageIndex])
        }
        
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = MaterialTheme.colorScheme.primary
        )

        if (pagerState.currentPage == onboardingPages.size - 1) {
            Button(
                onClick = onOnboardingFinished,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Mulai Sekarang", style = MaterialTheme.typography.labelLarge)
            }
        } else {
            Spacer(modifier = Modifier.height(50.dp + 48.dp))
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = page.title,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    ChilliLeafDiseaseDetectionAppTheme {
        OnboardingScreen(onOnboardingFinished = {})
    }
}