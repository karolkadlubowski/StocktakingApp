package pl.polsl.stocktakingApp.presentation.configuration

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

    fun changeCodeType(codeType: CodeType) {
        _state.value = ConfigScreenState.InitialState(codeType)
    }
}


sealed class ConfigScreenState {
    abstract val codeType: CodeType

    data class InitialState(
        override val codeType: CodeType = CodeType.QR,
    ) : ConfigScreenState()

    //object ReadyState : ScanScreenState()
}