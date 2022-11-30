package pl.polsl.stocktakingApp.presentation.modifyObject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ModifyObjectScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ModifyObjectScreenState>(_coroutineDispatcher) {
    override val initialState: ModifyObjectScreenState = ModifyObjectScreenState.InitialState
    override val _state: MutableStateFlow<ModifyObjectScreenState> = MutableStateFlow(initialState)
}

sealed class ModifyObjectScreenState {
    object InitialState : ModifyObjectScreenState()
    object ReadyState : ModifyObjectScreenState()
}