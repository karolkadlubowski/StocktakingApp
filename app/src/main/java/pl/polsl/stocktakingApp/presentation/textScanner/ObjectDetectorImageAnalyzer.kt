package pl.polsl.stocktakingApp.presentation.textScanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer

class ObjectDetectorImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val onRegexFound: (String) -> Unit,
    private val regex: Regex? = "[a-zA-Z][a-zA-Z][a-zA-Z]-\\d\\d\\d\\d\\d\\d\\d".toRegex()
) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null && regex != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

//            val text = "STA-0000046 STA-0000046"
//
//            val r = "[a-zA-Z][a-zA-Z][a-zA-Z]-\\d\\d\\d\\d\\d\\d\\d".toRegex()
//            val matches = r.findAll(text)

//            val data = matches.map { it.value }
//                .groupBy { it }
//                .map { Pair(it.key, it.value.size) }
//                .sortedByDescending { it.second }
//                .take(10)
//
//            for ((word, freq) in data) {
//
//                System.out.printf("%s %d \n", word, freq)
//            }

            textRecognizer.process(image)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val foundString = it.result?.text ?: ""
                        if (!foundString.isNullOrEmpty()) {
                            var foundPattern = regex.find(foundString)

//                            if(foundPattern==null){
//                                val editedString = foundString.replace('D','0')
//                                val editedString2 = editedString.replace('O','0')
//                                foundPattern = regex.find(editedString2)
//                            }

                            if (foundPattern != null) {
                                onRegexFound(foundPattern.value)
                            }
                        }
                        //extractedText.value = it.result?.text ?: ""
                    }
                    imageProxy.close()
                }
        }
    }
}