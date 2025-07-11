package com.example.chillileafdiseasedetectionapp.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class DiseaseInfo(
    val description: String,
    val recommendation: String
)

fun getDiseaseDetails(diseaseName: String): DiseaseInfo {
    return when (diseaseName) {
        "Bacterial Spot" -> DiseaseInfo(
            description = "Penyakit ini disebabkan oleh infeksi dari tiga spesies bakteri Xanthomonas, yaitu X. euvesicatoria, X. perforans, dan X. gardneri. Daun yang terinfeksi seringkali rontok sebelum waktunya, yang dapat mengurangi kanopi tanaman dan menyebabkan buah terbakar sinar matahari (sun scald).",
            recommendation = "Jaga kebersihan kebun, hindari penyiraman berlebih pada daun, dan buang bagian tanaman yang terinfeksi. Hindari penggunaan irigasi dari atas (overhead irrigation) karena air berperan penting dalam penyebaran penyakit."
        )
        "Cercospora Leaf Spot" -> DiseaseInfo(
            description = "Penyakit ini disebabkan oleh jamur Cercospora sp. Ditandai dengan bercak bulat berwarna coklat dengan bagian tengah berwarna abu-abu pucat dan tepi kehitaman. Biasanya penyakit ini menyerang saat musim hujan.",
            recommendation = "Lakukan sanitasi dengan membuang tanaman terinfeksi, gunakan benih bebas patogen, dan pilih waktu tanam yang tepat (seperti saat kemarau dengan irigasi baik). Gunakan fungisida sesuai anjuran untuk pengendalian lebih lanjut."
        )
        "Curl Virus" -> DiseaseInfo(
            description = "Dikenal juga sebagai Virus Kuning atau Penyakit Keriting. Penyakit ini disebabkan oleh virus Gemini yang ditularkan oleh serangga kutu kebul (Bemisia Tabaci). Penyakit ini menyebabkan daun menjadi keriting, kaku, dan ukurannya mengecil, yang dapat menyebabkan kegagalan pembuahan.",
            recommendation = "Gunakan benih sehat yang tahan virus, lakukan rotasi tanaman, dan jaga kebersihan lahan (sanitasi). Segera cabut dan musnahkan tanaman yang terinfeksi untuk mencegah penyebaran. Kendalikan serangga pembawa virus (kutu kebul) dan lakukan pemupukan berimbang."
        )
        "Nutrition Deficiency" -> DiseaseInfo(
            description = "Kondisi ini bukan penyakit menular, melainkan gangguan fisiologis akibat tanaman kekurangan unsur hara penting seperti Nitrogen (N), Fosfor (P), atau Kalium (K). Kekurangan unsur hara ini menyebabkan perubahan warna dan kematian jaringan pada daun.",
            recommendation = "Lakukan pemupukan yang seimbang menggunakan pupuk NPK dan periksa pH tanah, karena pH yang tidak sesuai dapat menghambat penyerapan nutrisi."
        )
        "White Spot" -> DiseaseInfo(
            description = "Gejala bercak atau lapisan putih pada daun cabai yang disebabkan oleh serangan hama Lalat Pengorok daun (Liriomyza sp). Lalat Pengorok daun biasanya berkembang saat kemarau dan memiliki siklus hidup 22-25 hari dengan stadium larva selama 6-12 hari. Serangan hama ini menghambat fotosintesis dan dapat membuat daun rontok jika sudah parah.",
            recommendation = "Upaya pengendalian dapat dilakukan dengan memasang tanaman perangkap dan perangkap kuning, melakukan sanitasi lahan, serta menerapkan sistem tumpangsari."
        )
        else -> DiseaseInfo( // Mencakup 'Healthy Leaf' atau jika tidak dikenal
            description = "Daun cabai Anda terdeteksi dalam kondisi baik dan sehat. Tidak ada gejala penyakit signifikan yang ditemukan berdasarkan analisis gambar.",
            recommendation = "Lanjutkan perawatan yang baik dengan penyiraman, pemupukan, dan pemantauan rutin untuk menjaga tanaman tetap sehat."
        )
    }
}

@Composable
fun ResultCard(disease: String, accuracy: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Hasil Deteksi",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = disease,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Divider(modifier = Modifier.padding(horizontal = 24.dp))
            Text(
                text = "Tingkat Keyakinan: $accuracy",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}