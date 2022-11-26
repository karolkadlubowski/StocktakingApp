package pl.polsl.stocktakingApp.presentation.settings

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer

class ObjectDetectorImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val extractedText: MutableState<String>
) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            textRecognizer.process(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        extractedText.value = it.result?.text ?: ""
                    }
                    imageProxy.close()
                }
        }
    }
}