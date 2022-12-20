package pl.polsl.stocktakingApp.presentation.textScanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import pl.polsl.stocktakingApp.domain.services.RegexService

class ObjectDetectorImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val onRegexFound: (String) -> Unit,
    private val regexString: String?,// = "[a-zA-Z][a-zA-Z][a-zA-Z]-\\d\\d\\d\\d\\d\\d\\d".toRegex()
    private val regexService: RegexService = RegexService()
) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null && !regexString.isNullOrEmpty()) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val regex = regexService.rewriteStringToRegex(regexString)

            textRecognizer.process(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val foundString = it.result.text.uppercase()
                        if (foundString.isNotEmpty()) {
                            var foundPattern = regex.find(foundString)?.value

                            if (foundPattern == null) {
                                foundPattern = switchSigns(foundString, regexString, regex)
                            }

                            if (foundPattern != null) {
                                onRegexFound(foundPattern)
                            }
                        }
                    }
                    imageProxy.close()
                }
        }
    }

    private fun switchSigns(scannedString: String, regexString: String, regex: Regex): String? {
        val dReplacedForZero = scannedString.replace('D', '0')
        val oReplacedForZero = dReplacedForZero.replace('O', '0')
        var foundPattern = regex.find(oReplacedForZero)

        if (foundPattern != null) {
            return foundPattern.value
        }

        val tReplacedForFour = scannedString.replace('T', '4')

        foundPattern = regex.find(tReplacedForFour)

        return foundPattern?.value
    }

}