package pl.polsl.stocktakingApp.presentation.configuration

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import pl.polsl.printer.DataResult
import pl.polsl.printer.Result
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.domain.usecase.*
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel
import pl.polsl.stocktakingApp.presentation.common.Event
import javax.inject.Inject

@HiltViewModel
class ConfigScreenViewModel @Inject constructor(
    private val _saveSelectedPrinter: SaveSelectedPrinter,
    private val _provideBluetoothConnection: ProvideBluetoothConnection,
    private val _getBondedDevices: GetBondedDevices,
    private val _setLabelCodeType: SetLabelCodeType,
    private val _setExampleNumber: SetExampleNumber,
    _observeExampleNumber: ObserveExampleNumber,
    _observeSelectedPrinter: ObserveSelectedPrinter,
    _observeLabelCodeType: ObserveLabelCodeType,
    _coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel<ConfigScreenState>(_coroutineDispatcher) {
    override val initialState: ConfigScreenState = ConfigScreenState.InitialState()

    private val _bondedDeviceList: MutableStateFlow<List<BluetoothDevice>> = MutableStateFlow(
        listOf()
    )

    private val _codeType: Flow<CodeType> = _observeLabelCodeType(Unit)

    private val _selectedPrinterAddress: Flow<String?> = _observeSelectedPrinter(Unit)

    private val _exampleNumber: Flow<String?> = _observeExampleNumber(Unit)

    override val _state: Flow<ConfigScreenState> = combine(
        flow = _codeType,
        flow2 = _selectedPrinterAddress,
        flow3 = _bondedDeviceList,
        flow4 = _exampleNumber
    ) { codeType, selectedPrinterAddress, bondedDeviceList, exampleNumber ->
        ConfigScreenState.ReadyState(
            codeType,
            bondedDeviceList,
            selectedPrinterAddress,
            exampleNumber
        )
    }


    fun changeCodeType(codeType: CodeType) = launch {
        _setLabelCodeType(codeType)
    }

    fun changeSelectedDevice(device: BluetoothDevice) =
        launch { _saveSelectedPrinter(SaveSelectedPrinter.Params(device.address)) }

    fun changeExampleNumber(number: String) = launch { _setExampleNumber(number) }

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
    abstract val exampleNumber: String?

    data class InitialState(
        override val codeType: CodeType = CodeType.QR,
        override val bluetoothDeviceList: List<BluetoothDevice> = listOf(),
        override val selectedPrinterAddress: String? = null,
        override val exampleNumber: String? = null
    ) : ConfigScreenState()

    data class ReadyState(
        override val codeType: CodeType,
        override val bluetoothDeviceList: List<BluetoothDevice>,
        override val selectedPrinterAddress: String?,
        override val exampleNumber: String?
    ) : ConfigScreenState()
}