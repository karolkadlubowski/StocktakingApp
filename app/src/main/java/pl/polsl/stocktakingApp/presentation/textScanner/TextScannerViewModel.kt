package pl.polsl.stocktakingApp.presentation.textScanner


import android.net.Uri
import androidx.core.net.toFile
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.services.RegexService
import pl.polsl.stocktakingApp.domain.usecase.GetObjectByBarcode
import pl.polsl.stocktakingApp.domain.usecase.ObserveExampleNumber
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import pl.polsl.stocktakingApp.presentation.common.Event
import javax.inject.Inject

@HiltViewModel
class TextScannerScreenViewModel @Inject constructor(
    private val _regexService: RegexService,
    private val _getObjectByBarcode: GetObjectByBarcode,
    _observeRegex: ObserveExampleNumber,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<TextScannerScreenState>(_coroutineDispatcher) {
    override val initialState: TextScannerScreenState = TextScannerScreenState.Scanning
    override val _state: MutableStateFlow<TextScannerScreenState> = MutableStateFlow(initialState)

    private var _regex: Flow<String?> = _observeRegex(Unit)

    private val _recognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun onPhotoTaken(photoUri: Uri) {
        _state.value = TextScannerScreenState.Cropping(photoUri)
    }

    fun onNumberFound(objectNumber: String) {
        _state.value = TextScannerScreenState.Found(objectNumber)
    }

    fun analyzePhoto(inputImage: InputImage) = launch {
        _deletePhoto()
        val regexString = _regex.first()

        _recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val foundString =
                    _regexService.getStocktakingNumberFromText(regexString, visionText.text)
                _state.value = TextScannerScreenState.Found(foundString)
            }
            .addOnFailureListener { e ->
                _state.value = TextScannerScreenState.Scanning
                launch { _events.emit(Event.Message(R.string.textNotRecognized)) }
            }
    }

    private fun _deletePhoto() {
        val state = _state.value
        if (state is TextScannerScreenState.Cropping) {
            state.takenPhotoUri.toFile().delete()
        }
    }

    fun continueScanning() {
        _state.value = TextScannerScreenState.Scanning
    }

    fun onBarcodeRecognized(barcode: String) = launch {
        val stocktakingObject = _getObjectByBarcode(barcode)
        if (stocktakingObject != null) {
            _events.emit(ObjectExists(stocktakingObject))
        } else {
            _events.emit((ObjectNotExists))
        }
    }

    object ObjectNotExists : Event.Message(R.string.ObjectNotExistsEvent)
    data class ObjectExists(val stocktakingObject: StocktakingObject) : Event()
}

sealed class TextScannerScreenState {
    object Scanning : TextScannerScreenState()
    data class Cropping(val takenPhotoUri: Uri) : TextScannerScreenState()
    data class Found(val foundNumber: String) : TextScannerScreenState()
}