package pl.polsl.stocktakingApp.presentation.settings

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.presentation.scan.observeState
import java.io.File
import java.util.concurrent.Executors

@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text("Settings Screen")
//    }

    val state by viewModel.observeState()

    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    val context = LocalContext.current

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            //imageUri = result.uriContent
            viewModel.analyzePhoto(
                InputImage.fromFilePath(context, result.uriContent!!),
                result.uriContent!!
            )
        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }


    when (state) {
        is SettingsScreenState.Scanning -> MLKitTextRecognition(
            onTakePhoto = viewModel::onPhotoTaken,
            textRecognizer = textRecognizer
        )
        is SettingsScreenState.Cropping -> {
//            ImageCropper(
//                onImageCropped = viewModel::analyzePhoto,
//                context
            imageCropLauncher.launch(
                CropImageContractOptions(
                    (state as SettingsScreenState.Cropping).takenPhotoUri,
                    CropImageOptions()
                )
            )
        }
        is SettingsScreenState.Found -> {
            Column {
                AsyncImage(
                    modifier = Modifier.weight(1f),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data((state as SettingsScreenState.Found).finalPhotoUri).apply {
                            if (LocalInspectionMode.current) {
                                placeholder(R.drawable.placeholder)
                            }
                        }.build(),
                    contentDescription = "Preview of image taken",
                    contentScale = ContentScale.Crop,
                )

                Text(
                    text = (state as SettingsScreenState.Found).foundId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MLKitTextRecognition(
    onTakePhoto: (Uri) -> Unit,
    textRecognizer: TextRecognizer
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val extractedText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val imageCapture = remember {
            ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build()
        }
        TextRecognitionView(
            context = context,
            lifecycleOwner = lifecycleOwner,
            extractedText = extractedText,
            onTakePhoto = onTakePhoto,
            imageCapture = imageCapture,
            textRecognizer = textRecognizer
        )

        //CameraView(modifier = Modifier, imageCapture = imageCapture)
//
//        Text(
//            text = extractedText.value,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White)
//                .padding(16.dp)
//        )

        Button(onClick = { makeFile(context, imageCapture, onTakePhoto) }) {
            Text(text = "make photo")
        }
    }
}

@Composable
fun TextRecognitionView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    extractedText: MutableState<String>,
    onTakePhoto: (Uri) -> Unit,
    imageCapture: ImageCapture,
    textRecognizer: TextRecognizer
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val executor = ContextCompat.getMainExecutor(context)
    val cameraProvider = cameraProviderFuture.get()
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

//    val imageCapture = remember {
//        ImageCapture.Builder()
//            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
//            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
//            .build()
//    }

    Box {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(
                                cameraExecutor,
                                ObjectDetectorImageAnalyzer(textRecognizer, extractedText)
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .align(Alignment.TopStart)
        ) {
            IconButton(
                onClick = {
                    makeFile(context, imageCapture, onTakePhoto)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = Color.White
                )
            }
        }
    }
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
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
//                val message = when (exception.imageCaptureError) {
//                    else -> Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
//                }
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoTaken(Uri.fromFile(photoFile))
            }
        },
    )
}