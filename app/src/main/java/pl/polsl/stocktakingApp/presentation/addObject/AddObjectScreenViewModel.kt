package pl.polsl.stocktakingApp.presentation.addObject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AddObjectScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<AddObjectScreenState>(_coroutineDispatcher) {
    override val initialState: AddObjectScreenState = AddObjectScreenState.InitialState
    override val _state: MutableStateFlow<AddObjectScreenState> = MutableStateFlow(initialState)
}

sealed class AddObjectScreenState {
    object InitialState : AddObjectScreenState()
    object ReadyState : AddObjectScreenState()
}