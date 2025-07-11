package com.example.chillileafdiseasedetectionapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File

class ImageClassifierHelper(
    private val modelName: String = "final_chili_leaf_disease_classifier.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(BaseOptions.builder().useNnapi().build())
            .setMaxResults(1)

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError("Gagal menginisialisasi classifier: ${e.message}")
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyImage(image: Bitmap) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        var inferenceTime = SystemClock.uptimeMillis()

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))
        val results = imageClassifier?.classify(tensorImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        classifierListener?.onResults(results, inferenceTime)
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<org.tensorflow.lite.task.vision.classifier.Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"

        fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            val fileName = "analysis_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            val outputStream = file.outputStream()
            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            return Uri.fromFile(file)
        }
    }
}