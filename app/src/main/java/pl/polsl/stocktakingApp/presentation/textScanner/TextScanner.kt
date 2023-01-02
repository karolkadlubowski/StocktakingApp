package pl.polsl.stocktakingApp.presentation.textScanner

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.text.TextRecognizer
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.ui.theme.C
import java.io.File
import java.util.concurrent.Executors


@Composable
fun MLKitTextRecognition(
    onTakePhoto: (Uri) -> Unit,
    textRecognizer: TextRecognizer,
    onTextRecognized: (String) -> Unit,
    regex: String?,
    barcodeScanner: BarcodeScanner,
    onBarcodeRecognized: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val imageCapture = remember {
        ImageCapture.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        TextRecognitionView(
            context = context,
            lifecycleOwner = lifecycleOwner,
            onTextRecognized = onTextRecognized,
            imageCapture = imageCapture,
            textRecognizer = textRecognizer,
            regex = regex,
            barcodeScanner = barcodeScanner,
            onBarcodeRecognized = onBarcodeRecognized
        )

        IconButton(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                makeFile(context, imageCapture, onTakePhoto)
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lens),
                    contentDescription = "Take picture",
                    tint = C.statusBarColor,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(1.dp)
                        .border(1.dp, C.statusBarColor, CircleShape)
                )
            })
    }
}

@Composable
fun TextRecognitionView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onTextRecognized: (String) -> Unit,
    imageCapture: ImageCapture,
    textRecognizer: TextRecognizer,
    regex: String?,
    barcodeScanner: BarcodeScanner,
    onBarcodeRecognized: (String) -> Unit
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val executor = ContextCompat.getMainExecutor(context)
    val cameraProvider = cameraProviderFuture.get()
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                        setAnalyzer(
                            cameraExecutor,
                            LiveImageAnalyzer(
                                textRecognizer = textRecognizer,
                                onRegexFound = onTextRecognized,
                                regexString = regex,
                                barcodeScanner = barcodeScanner,
                                onNumberFromBarcodeFound = onBarcodeRecognized
                            )
                        )
                    }
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    imageAnalysis,
                    preview,
                    imageCapture
                )
            }, executor)
            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            previewView
        }
    )
}

private fun makeFile(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoTaken: (Uri) -> Unit,
) {

    val imageDirectory = File(context.filesDir.path + "/images")
    if (!imageDirectory.exists()) {
        imageDirectory.mkdir()
    }

    val photoFile = File(
        context.filesDir.path + "/images",
        "${System.currentTimeMillis()}.jpg"
    )
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoTaken(Uri.fromFile(photoFile))
            }
        },
    )
}