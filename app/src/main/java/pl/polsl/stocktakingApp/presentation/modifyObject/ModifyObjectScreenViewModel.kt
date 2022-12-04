package pl.polsl.stocktakingApp.presentation.modifyObject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.UpsertObject
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ModifyObjectScreenViewModel @Inject constructor(
    private val _upsertObject: UpsertObject,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ModifyObjectScreenState>(_coroutineDispatcher) {
    override val initialState: ModifyObjectScreenState = ModifyObjectScreenState.InitialState
    override val _state: MutableStateFlow<ModifyObjectScreenState> = MutableStateFlow(initialState)

    fun upsertObject(stocktakingObject: StocktakingObject) =
        launch { _upsertObject(stocktakingObject) }
}

sealed class ModifyObjectScreenState {
    object InitialState : ModifyObjectScreenState()
    object ReadyState : ModifyObjectScreenState()
}