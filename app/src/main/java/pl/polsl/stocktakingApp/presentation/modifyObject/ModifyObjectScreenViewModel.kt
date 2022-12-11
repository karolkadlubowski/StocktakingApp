package pl.polsl.stocktakingApp.presentation.modifyObject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
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

    private val _isAddScreen: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _stocktakingObject: MutableStateFlow<StocktakingObject> =
        MutableStateFlow(StocktakingObject())
//    private val _name: MutableStateFlow<String> = MutableStateFlow("")
//    private val _barcode: MutableStateFlow<String> = MutableStateFlow("")
//    private val _description: MutableStateFlow<String> = MutableStateFlow("")
//    private val _amount: MutableStateFlow<Int> = MutableStateFlow(1)

//    override val _state: Flow<ModifyObjectScreenState> = combine(
//        combine(_name, _barcode, _description, ::Triple),
//        combine(_amount, _isAddScreen, ::Pair)
//    ) { t1, t2 ->
//        if (t2.second) {
//            ModifyObjectScreenState.AddObjectState(t1.first, t1.second, t1.third, t2.first)
//        } else {
//            ModifyObjectScreenState.EditObjectState(t1.first, t1.second, t1.third, t2.first)
//        }
//    }

    override val _state: Flow<ModifyObjectScreenState> =
        _isAddScreen.combine(_stocktakingObject) { isAddScreen, _stocktakingObject ->
            if (isAddScreen)
                ModifyObjectScreenState.AddObjectState(_stocktakingObject)
            else
                ModifyObjectScreenState.EditObjectState(_stocktakingObject)
        }


    fun init(stocktakingObject: StocktakingObject?) {
        if (stocktakingObject == null) {
            _isAddScreen.update { true }
        } else {
            _isAddScreen.update { false }
            _stocktakingObject.update { stocktakingObject }
//            stocktakingObject.apply {
//                _name.update { name }
//                _barcode.update { barcode }
//                _description.update { description }
//                _amount.update { amount }
//            }
        }
    }

    fun upsertObject() =
        launch {
            _upsertObject(
                _stocktakingObject.value
            )
        }

    fun setName(name: String) {
        _stocktakingObject.value.name = name
    }

    fun setBarcode(barcode: String) {
        _stocktakingObject.value.barcode = barcode
    }

    fun setDescription(description: String) {
        _stocktakingObject.value.description = description
    }

    fun setAmount(amount: Int) {
        _stocktakingObject.value.amount = amount
    }
}

sealed class ModifyObjectScreenState {
    abstract val stocktakingObject: StocktakingObject

    object InitialState : ModifyObjectScreenState() {
        override val stocktakingObject = StocktakingObject()
    }

    data class AddObjectState(
        override val stocktakingObject: StocktakingObject = StocktakingObject()
    ) : ModifyObjectScreenState()

    data class EditObjectState(
        override val stocktakingObject: StocktakingObject
    ) : ModifyObjectScreenState()
}