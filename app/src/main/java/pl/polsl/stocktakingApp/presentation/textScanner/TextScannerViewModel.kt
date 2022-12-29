package pl.polsl.stocktakingApp.presentation.textScanner


import android.net.Uri
import androidx.core.net.toFile
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.domain.services.RegexService
import pl.polsl.stocktakingApp.domain.usecase.ObserveExampleNumber
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import pl.polsl.stocktakingApp.presentation.common.Event
import javax.inject.Inject

@HiltViewModel
class TextScannerScreenViewModel @Inject constructor(
    private val _regexService: RegexService,
    _observeRegex: ObserveExampleNumber,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<TextScannerScreenState>(_coroutineDispatcher) {
    override val initialState: TextScannerScreenState = TextScannerScreenState.Scanning
    override val _state: MutableStateFlow<TextScannerScreenState> = MutableStateFlow(initialState)

    private var _regex: Flow<String?> = _observeRegex(Unit)

    private val _recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun onPhotoTaken(photoUri: Uri) {
        _state.value = TextScannerScreenState.Cropping(photoUri)
    }

    fun onIdFound(id: String) {
        _state.value = TextScannerScreenState.Found(id)
    }

    fun analyzePhoto(inputImage: InputImage) = launch {
        deletePhoto()
        val regexString = _regex.first()

        _recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val foundString = visionText.text
                if (regexString != null) {
                    val regex = _regexService.rewriteStringToRegex(regexString)
                    var foundPattern = regex.find(foundString)?.value

                    if (foundPattern == null) {
                        foundPattern = _regexService.switchSigns(foundString, regex)
                    }

                    if (foundPattern != null) {
                        _state.value = TextScannerScreenState.Found(foundPattern)
                    } else {
                        _state.value = TextScannerScreenState.Found(foundString)
                    }
                } else {
                    _state.value = TextScannerScreenState.Found(foundString)
                }
            }
            .addOnFailureListener { e ->
                _state.value = TextScannerScreenState.Scanning
                launch { _events.emit(Event.Message(R.string.textNotRecognized)) }
            }
    }

    private fun deletePhoto() {
        val state = _state.value
        if (state is TextScannerScreenState.Cropping) {
            state.takenPhotoUri.toFile().delete()
        }
    }

    fun continueScanning() {
        _state.value = TextScannerScreenState.Scanning
    }
}

sealed class TextScannerScreenState {
    object Scanning : TextScannerScreenState()
    data class Cropping(val takenPhotoUri: Uri) : TextScannerScreenState()
    data class Found(val foundId: String) : TextScannerScreenState()
}