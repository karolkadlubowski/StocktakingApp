package pl.polsl.stocktakingApp.presentation.modifyObject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.CheckIfObjectExists
import pl.polsl.stocktakingApp.domain.usecase.DeleteObject
import pl.polsl.stocktakingApp.domain.usecase.UpsertObject
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import pl.polsl.stocktakingApp.presentation.common.Event
import javax.inject.Inject

@HiltViewModel
class ModifyObjectScreenViewModel @Inject constructor(
    private val _upsertObject: UpsertObject,
    private val _deleteObject: DeleteObject,
    private val _checkIfObjectExists: CheckIfObjectExists,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ModifyObjectScreenState>(_coroutineDispatcher) {
    override val initialState: ModifyObjectScreenState = ModifyObjectScreenState.InitialState

    private val _isAddScreen: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _id: MutableStateFlow<Int?> =
        MutableStateFlow(null)
    private val _name: MutableStateFlow<String> =
        MutableStateFlow("")
    private val _barcode: MutableStateFlow<String> =
        MutableStateFlow("")
    private val _description: MutableStateFlow<String> =
        MutableStateFlow("")
    private val _amount: MutableStateFlow<String> =
        MutableStateFlow("1")

    override val _state: Flow<ModifyObjectScreenState> = combine(
        flow = _name,
        flow2 = _barcode,
        flow3 = _description,
        flow4 = _amount,
        flow5 = _isAddScreen
    ) { name, barcode, description, amount, isAddScreen ->
        if (isAddScreen)
            ModifyObjectScreenState.AddObjectState(name, barcode, description, amount)
        else
            ModifyObjectScreenState.EditObjectState(name, barcode, description, amount)
    }

    fun init(stocktakingObject: StocktakingObject?) {
        if (stocktakingObject == null) {
            _isAddScreen.update { true }
        } else {
            _isAddScreen.update { false }
            _id.update { stocktakingObject.id }
            _name.update { stocktakingObject.name }
            _amount.update { stocktakingObject.amount.toString() }
            _description.update { stocktakingObject.description }
            _barcode.update { stocktakingObject.barcode }
        }
    }

    fun upsertObject() =
        launch {
            if (_checkIfObjectExists(_barcode.value) && state.value is ModifyObjectScreenState.AddObjectState) {
                _events.emit(ObjectAlreadyExists)
                return@launch
            }
            if (_name.value.isNotBlank() && _barcode.value.isNotBlank() && _amount.value.toIntOrNull() != null) {
                _upsertObject(
                    StocktakingObject(
                        _id.value,
                        _name.value,
                        _description.value,
                        _amount.value.toInt(),
                        _barcode.value
                    )
                )
                _events.emit(ObjectUpsertSuccess)
            } else {
                _events.emit(EmptyFields)
            }
        }

    fun deleteObject() = launch {
        _deleteObject(
            DeleteObject.Params(
                StocktakingObject(
                    _id.value,
                    _name.value,
                    _description.value,
                    _amount.value.toInt(),
                    _barcode.value
                )
            )
        )
    }

    fun setName(name: String) {
        _name.update { name }
    }

    fun setBarcode(barcode: String) {
        _barcode.update { barcode }
    }

    fun setDescription(description: String) {
        _description.update { description }
    }

    fun setAmount(amount: String) {
        _amount.update { amount }
    }

    object EmptyFields : Event.Message(R.string.EmptyFieldsEvent)
    object ObjectUpsertSuccess : Event()
    object ObjectAlreadyExists : Event.Message(R.string.ObjectAlreadyExistsEvent)
}

sealed class ModifyObjectScreenState {
    abstract val name: String
    abstract val barcode: String
    abstract val description: String
    abstract val amount: String

    object InitialState : ModifyObjectScreenState() {
        override val name: String = ""
        override val barcode: String = ""
        override val description: String = ""
        override val amount: String = "1"
    }

    data class AddObjectState(
        override val name: String,
        override val barcode: String,
        override val description: String,
        override val amount: String
    ) : ModifyObjectScreenState()

    data class EditObjectState(
        override val name: String,
        override val barcode: String,
        override val description: String,
        override val amount: String
    ) : ModifyObjectScreenState()
}