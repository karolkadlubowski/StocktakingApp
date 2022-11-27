package pl.polsl.stocktakingApp.presentation.configuration

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ConfigScreenState>(_coroutineDispatcher) {
    override val initialState: ConfigScreenState = ConfigScreenState.InitialState()
    override val _state: MutableStateFlow<ConfigScreenState> = MutableStateFlow(initialState)

    fun changeUri(uri: Uri?) {
        _state.value = ConfigScreenState.InitialState(uri)
    }
}


sealed class ConfigScreenState {
    abstract val uri: Uri?

    data class InitialState(
        override val uri: Uri? = null
    ) : ConfigScreenState()

    //object ReadyState : ScanScreenState()
}