package pl.polsl.stocktakingApp.presentation.configuration

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import pl.polsl.printer.DataResult
import pl.polsl.printer.Result
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.domain.usecase.GetBondedDevices
import pl.polsl.stocktakingApp.domain.usecase.ProvideBluetoothConnection
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigScreenViewModel @Inject constructor(
    private val _provideBluetoothConnection: ProvideBluetoothConnection,
    private val _getBondedDevices: GetBondedDevices,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ConfigScreenState>(_coroutineDispatcher) {
    override val initialState: ConfigScreenState = ConfigScreenState.InitialState()
    override val _state: MutableStateFlow<ConfigScreenState> =
        MutableStateFlow(initialState)

    private val _bondedDeviceList: MutableStateFlow<List<BluetoothDevice>> = MutableStateFlow(
        listOf()
    )

    //private val _codeType: Flow<CodeType> = _observeLabelCodeTypeUseCase(Unit)


    fun changeCodeType(codeType: CodeType) {
        _state.value = ConfigScreenState.InitialState(codeType)
    }

    fun changeSelectedDevice(device: BluetoothDevice) {
        _state.value =
            (_state.value as ConfigScreenState.InitialState).copy(selectedPrinterAddress = device.address)
    }

    suspend fun updateListOfBondedDevices() {
        try {
            if (_provideBluetoothConnection(Unit) !is Result.Successful) {
                _events.emit(Event.Message(R.string.bluetoothEnablingError))
                return
            }

            val result = _getBondedDevices(Unit)
            if (result !is DataResult.Successful) {
                _events.emit(Event.Message(R.string.getBondedDevicesError))
                return
            }

            _bondedDeviceList.update { result.data }
        } catch (e: Exception) {
            e
        }
    }
}


sealed class ConfigScreenState {
    abstract val codeType: CodeType
    abstract val bluetoothDeviceList: List<BluetoothDevice>
    abstract val selectedPrinterAddress: String?

    data class InitialState(
        override val codeType: CodeType = CodeType.QR,
        override val bluetoothDeviceList: List<BluetoothDevice> = listOf(
//            BluetoothDevice("Sluchawki", "60:12:77:84"),
//            BluetoothDevice("Drukarka", "77:60:13:84"),
//            BluetoothDevice("Lodowka", "4F:12:60:77")
        ),
        override val selectedPrinterAddress: String? = null
    ) : ConfigScreenState()

    data class ReadyState(
        override val codeType: CodeType,
        override val bluetoothDeviceList: List<BluetoothDevice>,
        override val selectedPrinterAddress: String?
    ) : ConfigScreenState()
}