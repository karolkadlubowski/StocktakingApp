package pl.polsl.stocktakingApp.presentation.modifyObject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.DeleteObject
import pl.polsl.stocktakingApp.domain.usecase.UpsertObject
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ModifyObjectScreenViewModel @Inject constructor(
    private val _upsertObject: UpsertObject,
    private val _deleteObject: DeleteObject,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ModifyObjectScreenState>(_coroutineDispatcher) {
    override val initialState: ModifyObjectScreenState = ModifyObjectScreenState.InitialState

    private val _isAddScreen: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _stocktakingObject: MutableStateFlow<StocktakingObject> =
        MutableStateFlow(StocktakingObject())

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
        }
    }

    fun upsertObject() =
        launch {
            _upsertObject(
                _stocktakingObject.value
            )
        }

    fun deleteObject() = launch {
        _deleteObject(
            DeleteObject.Params(_stocktakingObject.value)
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