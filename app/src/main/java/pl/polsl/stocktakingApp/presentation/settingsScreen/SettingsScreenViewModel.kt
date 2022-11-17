package pl.polsl.stocktakingApp.presentation.settingsScreen


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<SettingsScreenState>(_coroutineDispatcher) {
    override val initialState: SettingsScreenState = SettingsScreenState.InitialState
    override val _state: MutableStateFlow<SettingsScreenState> = MutableStateFlow(initialState)
}

sealed class SettingsScreenState {
    object InitialState : SettingsScreenState()
    object ReadyState : SettingsScreenState()
}