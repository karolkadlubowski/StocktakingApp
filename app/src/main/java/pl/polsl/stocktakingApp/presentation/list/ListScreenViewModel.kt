package pl.polsl.stocktakingApp.presentation.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.ObserveObjectList
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val _observeObjectList: ObserveObjectList,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ListScreenState>(_coroutineDispatcher) {
    override val initialState: ListScreenState = ListScreenState.InitialState

    private var _list: DataFlow<List<StocktakingObject>> = _observeObjectList(Unit)

    override val _state: Flow<ListScreenState> = _list.map {
        ListScreenState.ReadyState(it.data)
    }
}

sealed class ListScreenState {
    object InitialState : ListScreenState()
    data class ReadyState(val list: List<StocktakingObject>) : ListScreenState()
}