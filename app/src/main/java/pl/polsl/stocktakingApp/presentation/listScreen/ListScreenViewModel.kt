package pl.polsl.stocktakingApp.presentation.listScreen

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ListScreenState>(_coroutineDispatcher) {
    override val initialState: ListScreenState = ListScreenState.InitialState
    override val _state: MutableStateFlow<ListScreenState> = MutableStateFlow(initialState)
}

sealed class ListScreenState {
    object InitialState : ListScreenState()
    object ReadyState : ListScreenState()
}