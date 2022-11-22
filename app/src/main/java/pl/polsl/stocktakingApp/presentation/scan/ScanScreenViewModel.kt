package pl.polsl.stocktakingApp.presentation.scan

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ScanScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ScanScreenState>(_coroutineDispatcher) {
    override val initialState: ScanScreenState = ScanScreenState.InitialState()
    override val _state: MutableStateFlow<ScanScreenState> = MutableStateFlow(initialState)

    fun changeUri(uri: Uri?) {
        _state.value = ScanScreenState.InitialState(uri)
    }
}


sealed class ScanScreenState {
    abstract val uri: Uri?

    data class InitialState(
        override val uri: Uri? = null
    ) : ScanScreenState()

    //object ReadyState : ScanScreenState()
}