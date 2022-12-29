package pl.polsl.stocktakingApp.presentation.textScanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import pl.polsl.stocktakingApp.domain.services.RegexService


class ImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val onRegexFound: (String) -> Unit,
    private val regexString: String?,
    private val regexService: RegexService = RegexService(),
    private val barcodeScanner: BarcodeScanner
) : ImageAnalysis.Analyzer {
    private val regex = regexString?.let { regexService.rewriteStringToRegex(it) }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes[0].rawValue?.let { onRegexFound(it.uppercase()) }
                        imageProxy.close()
                    }
                }.addOnFailureListener {
                    imageProxy.close()
                }

            if (!regexString.isNullOrEmpty()) {
                textRecognizer.process(image)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            val foundString =
                                result.result.text.uppercase().filterNot { it.isWhitespace() }
                            if (foundString.isNotEmpty()) {
                                var foundPattern = regex?.find(foundString)?.value

                                if (foundPattern == null) {
                                    foundPattern = regexService.switchSigns(foundString, regex!!)
                                }

                                if (foundPattern != null) {
                                    onRegexFound(foundPattern)
                                }
                            }
                        }
                        imageProxy.close()
                    }
                    .addOnFailureListener { imageProxy.close() }
            }
        }
    }
}