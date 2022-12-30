package pl.polsl.stocktakingApp.presentation.textScanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.presentation.common.Event
import pl.polsl.stocktakingApp.presentation.common.observeEvents
import pl.polsl.stocktakingApp.presentation.common.observeState
import pl.polsl.stocktakingApp.presentation.common.ui.SnackbarScreenWrapper
import pl.polsl.stocktakingApp.presentation.destinations.ModifyObjectScreenDestination
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Destination
@Composable
fun TextScannerScreen(
    navigator: DestinationsNavigator,
    viewModel: TextScannerScreenViewModel = hiltViewModel(),
    regex: String?
) {
    val state by viewModel.observeState()

    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    val barcodeScanner = remember { BarcodeScanning.getClient() }

    val context = LocalContext.current

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            viewModel.analyzePhoto(
                InputImage.fromFilePath(context, result.uriContent!!),
            )
        } else {
            viewModel.continueScanning()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.events.observeEvents {
        when (it) {
            is TextScannerScreenViewModel.ObjectExists -> {
                navigator.navigate(
                    ModifyObjectScreenDestination(
                        stocktakingObject = it.stocktakingObject
                    )
                )
            }
            is Event.Message -> snackbarHostState.showSnackbar(
                it.text(context)
            )
        }
    }

    SnackbarScreenWrapper(snackbarHostState = snackbarHostState) {
        when (state) {
            is TextScannerScreenState.Scanning -> MLKitTextRecognition(
                onTakePhoto = viewModel::onPhotoTaken,
                textRecognizer = textRecognizer,
                onTextRecognized = viewModel::onNumberFound,
                regex = regex?.let {
                    URLDecoder.decode(regex, StandardCharsets.UTF_8.toString())
                },
                onBackPressed = { navigator.popBackStack() },
                barcodeScanner = barcodeScanner,
                onBarcodeRecognized = viewModel::onBarcodeRecognized
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
                navigator.navigate(
                    ModifyObjectScreenDestination(
                        (state as TextScannerScreenState.Found).foundId
                    )
                )

                viewModel.continueScanning()
            }
        }
    }
}