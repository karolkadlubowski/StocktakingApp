package pl.polsl.stocktakingApp.presentation.textScanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import pl.polsl.stocktakingApp.domain.services.RegexService

class LiveImageAnalyzer(
    private val _textRecognizer: TextRecognizer,
    private val _onRegexFound: (String) -> Unit,
    private val _regexString: String?,
    private val _barcodeScanner: BarcodeScanner,
    private val _onNumberFromBarcodeFound: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val _regexService: RegexService = RegexService()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            _barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes[0].rawValue?.let { _onNumberFromBarcodeFound(it.uppercase()) }
                        imageProxy.close()
                    }
                }.addOnFailureListener {
                    imageProxy.close()
                }

            if (!_regexString.isNullOrEmpty()) {
                _textRecognizer.process(image)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            val foundString = _regexService.getStocktakingNumberOrNullFromText(
                                _regexString,
                                result.result.text
                            )

                            if (foundString != null) {
                                _onRegexFound(foundString)
                            }
                        }
                        imageProxy.close()
                    }
                    .addOnFailureListener { imageProxy.close() }
            }
        }
    }
}