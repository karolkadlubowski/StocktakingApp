package pl.polsl.stocktakingApp.presentation.settings


import android.net.Uri
import androidx.core.net.toFile
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<SettingsScreenState>(_coroutineDispatcher) {
    override val initialState: SettingsScreenState = SettingsScreenState.Scanning
    override val _state: MutableStateFlow<SettingsScreenState> = MutableStateFlow(initialState)

    private val _recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun onPhotoTaken(photoUri: Uri) {
        _state.value = SettingsScreenState.Cropping(photoUri)
    }

//    fun onPhotoCropped(photoUri: Uri) {
//        val result = analyzePhoto(photoUri)
//
//        if (result != null) {
//            _state.value = SettingsScreenState.Found(photoUri, result)
//        } else {
//            _state.value = SettingsScreenState.Scanning
//            launch { _events.emit(Event.Message(R.string.textNotRecognized)) }
//        }
//    }

    fun analyzePhoto(inputImage: InputImage, photoUri: Uri) {
        _recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                _state.value = SettingsScreenState.Found(photoUri, visionText.text)
            }
            .addOnFailureListener { e ->
                _state.value = SettingsScreenState.Scanning
                launch { _events.emit(Event.Message(R.string.textNotRecognized)) }
            }
    }

    fun continueScanning() {
        _state.value = SettingsScreenState.Scanning
    }

    fun rejectPhotoAndContinuePointing(photoUri: Uri) {
        photoUri.toFile().delete()
        continueScanning()
    }
}

sealed class SettingsScreenState {
    object Scanning : SettingsScreenState()
    data class Cropping(val takenPhotoUri: Uri) : SettingsScreenState()
    data class Found(val finalPhotoUri: Uri, val foundId: String) : SettingsScreenState()
}