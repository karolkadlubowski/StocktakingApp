package pl.polsl.stocktakingApp.presentation.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.polsl.printer.Result
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.GetLabelCodeType
import pl.polsl.stocktakingApp.domain.usecase.GetSelectedPrinter
import pl.polsl.stocktakingApp.domain.usecase.ObserveObjectList
import pl.polsl.stocktakingApp.domain.usecase.PrintLabel
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import pl.polsl.stocktakingApp.presentation.common.Event
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val printLabel: PrintLabel,
    private val getLabelCodeType: GetLabelCodeType,
    private val getSelectedPrinter: GetSelectedPrinter,
    _observeObjectList: ObserveObjectList,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ListScreenState>(_coroutineDispatcher) {
    override val initialState: ListScreenState = ListScreenState.InitialState

    private var _list: DataFlow<List<StocktakingObject>> = _observeObjectList(Unit)

    fun printLabel(stocktakingObject: StocktakingObject) = launch {
        val selectedPrinter = getSelectedPrinter(Unit)

        if (selectedPrinter != null) {
            if (printLabel.invoke(
                    PrintLabel.Params(
                        selectedPrinter,
                        stocktakingObject,
                        getLabelCodeType(Unit)
                    )
                ) !is Result.Successful
            ) {
                _events.emit(Event.Message(R.string.printLabelError))
            }
        } else {
            _events.emit(Event.NoSelectedPrinter)
        }

    }

    override val _state: Flow<ListScreenState> = _list.map {
        ListScreenState.ReadyState(it.data)
    }
}

sealed class ListScreenState {
    object InitialState : ListScreenState()
    data class ReadyState(val list: List<StocktakingObject>) : ListScreenState()
}