package pl.polsl.stocktakingApp.presentation.textScanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import pl.polsl.stocktakingApp.domain.services.RegexService

class LiveImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val onRegexFound: (String) -> Unit,
    private val regexString: String?,
    private val barcodeScanner: BarcodeScanner,
    private val onNumberFromBarcodeFound: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val _regexService: RegexService = RegexService()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes[0].rawValue?.let { onNumberFromBarcodeFound(it.uppercase()) }
                        imageProxy.close()
                    }
                }.addOnFailureListener {
                    imageProxy.close()
                }

            if (!regexString.isNullOrEmpty()) {
                textRecognizer.process(image)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            val foundString = _regexService.returnRegexStringOrNullFromString(
                                regexString,
                                result.result.text
                            )

                            if (foundString != null) {
                                onRegexFound(foundString)
                            }
                        }
                        imageProxy.close()
                    }
                    .addOnFailureListener { imageProxy.close() }
            }
        }
    }
}