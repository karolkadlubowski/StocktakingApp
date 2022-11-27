package pl.polsl.stocktakingApp.presentation.textScanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.presentation.common.ui.AddObjectDialog
import pl.polsl.stocktakingApp.presentation.configuration.observeState

@Destination
@Composable
fun TextScannerScreen(
    navigator: DestinationsNavigator,
    viewModel: TextScannerScreenViewModel = hiltViewModel()
) {
    val state by viewModel.observeState()

    var isDialogVisible = remember {
        false
    }

    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    val context = LocalContext.current

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            viewModel.analyzePhoto(
                InputImage.fromFilePath(context, result.uriContent!!),
                result.uriContent!!
            )
        } else {
            // TODO
            val exception = result.error
        }
    }


    when (state) {
        is TextScannerScreenState.Scanning -> MLKitTextRecognition(
            onTakePhoto = viewModel::onPhotoTaken,
            textRecognizer = textRecognizer
        )
        is TextScannerScreenState.Cropping -> {
            imageCropLauncher.launch(
                CropImageContractOptions(
                    (state as TextScannerScreenState.Cropping).takenPhotoUri,
                    CropImageOptions()
                )
            )
        }
        is TextScannerScreenState.Found -> {
            Column {
                AsyncImage(
                    modifier = Modifier.weight(1f),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data((state as TextScannerScreenState.Found).finalPhotoUri).apply {
                            if (LocalInspectionMode.current) {
                                placeholder(R.drawable.placeholder)
                            }
                        }.build(),
                    contentDescription = "Preview of image taken",
                    contentScale = ContentScale.Crop,
                )

                if (isDialogVisible) {
                    AddObjectDialog(
                        objectId = (state as TextScannerScreenState.Found).foundId,
                        onAccept = {
                            navigator.popBackStack()
                        },
                        onCancel = { isDialogVisible = false })
                }

//                Text(
//                    text = (state as TextScannerScreenState.Found).foundId,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.White)
//                        .padding(16.dp)
//                )
            }
        }
    }
}