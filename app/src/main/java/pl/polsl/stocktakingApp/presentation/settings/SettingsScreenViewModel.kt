package pl.polsl.stocktakingApp.presentation.settings


import android.net.Uri
import androidx.core.net.toFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<SettingsScreenState>(_coroutineDispatcher) {
    override val initialState: SettingsScreenState = SettingsScreenState.Searching
    override val _state: MutableStateFlow<SettingsScreenState> = MutableStateFlow(initialState)

    fun photoTaken(photoUri: Uri) {
        _state.value = SettingsScreenState.Taken(photoUri)
    }

    fun continuePointing() {
        _state.value = SettingsScreenState.Searching
    }

    fun rejectPhotoAndContinuePointing(photoUri: Uri) {
        photoUri.toFile().delete()
        continuePointing()
    }
}

sealed class SettingsScreenState {
    object Searching : SettingsScreenState()
    data class Taken(val photoUri: Uri) : SettingsScreenState()
}