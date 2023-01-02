package pl.polsl.stocktakingApp.presentation.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.common.Result
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.*
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import pl.polsl.stocktakingApp.presentation.common.Event
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val _printLabel: PrintLabel,
    private val _getLabelCodeType: GetLabelCodeType,
    private val _getSelectedPrinter: GetSelectedPrinter,
    private val _provideBluetoothConnection: ProvideBluetoothConnection,
    _observeRegex: ObserveExampleNumber,
    _observeObjectList: ObserveObjectList,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ListScreenState>(_coroutineDispatcher) {
    override val initialState: ListScreenState = ListScreenState.InitialState

    private var _regex = _observeRegex(Unit)

    private val _searchField: MutableStateFlow<String> = MutableStateFlow("")

    private var _list: DataFlow<List<StocktakingObject>> = _observeObjectList(_searchField)

    override val _state: Flow<ListScreenState> = combine(
        _list.map { it.data },
        _regex,
        _searchField
    ) { list, regex, searchField ->
        ListScreenState.ReadyState(list, regex, searchField)
    }

    fun changeSearchQuery(string: String) {
        _searchField.update { string }
    }

    fun printLabel(stocktakingObject: StocktakingObject) = launch {

        if (_provideBluetoothConnection(Unit) !is Result.Successful) {
            _events.emit(Event.BluetoothError)
            return@launch
        }

        val selectedPrinter = _getSelectedPrinter(Unit)

        if (selectedPrinter == null) {
            _events.emit(Event.NoSelectedPrinter)
            return@launch
        }


        if (_printLabel.invoke(
                PrintLabel.Params(
                    selectedPrinter,
                    stocktakingObject,
                    _getLabelCodeType(Unit)
                )
            ) !is Result.Successful
        ) {
            _events.emit(PrintLabelError)
        }
    }

    object PrintLabelError : Event.Message(R.string.printLabelError)
}

sealed class ListScreenState {
    abstract val regex: String?
    abstract val searchField: String

    object InitialState : ListScreenState() {
        override val regex: String? = null
        override val searchField: String = ""
    }

    data class ReadyState(
        val list: List<StocktakingObject>,
        override val regex: String?,
        override val searchField: String
    ) : ListScreenState()
}