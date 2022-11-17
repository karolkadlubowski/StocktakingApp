package pl.polsl.stocktakingApp.presentation.scanScreen

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ScanScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ScanScreenState>(_coroutineDispatcher) {
    override val initialState: ScanScreenState = ScanScreenState.InitialState
    override val _state: MutableStateFlow<ScanScreenState> = MutableStateFlow(initialState)
}

sealed class ScanScreenState {
    object InitialState : ScanScreenState()
    object ReadyState : ScanScreenState()
}