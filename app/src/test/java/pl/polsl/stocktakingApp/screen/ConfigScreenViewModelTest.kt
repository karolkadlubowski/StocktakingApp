package pl.polsl.stocktakingApp.screen

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import pl.polsl.stocktakingApp.base.BaseTest
import pl.polsl.stocktakingApp.common.DataResult
import pl.polsl.stocktakingApp.common.Result
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.domain.usecase.*
import pl.polsl.stocktakingApp.presentation.common.Event
import pl.polsl.stocktakingApp.presentation.configuration.CodeType
import pl.polsl.stocktakingApp.presentation.configuration.ConfigScreenState
import pl.polsl.stocktakingApp.presentation.configuration.ConfigScreenViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigScreenViewModelTest : BaseTest<ConfigScreenViewModel>() {
    private val saveSelectedPrinter: SaveSelectedPrinter = mockk()
    private val provideBluetoothConnection: ProvideBluetoothConnection = mockk()
    private val getBoundDevices: GetBoundDevices = mockk()
    private val setLabelCodeType: SetLabelCodeType = mockk()
    private val setExampleNumber: SetExampleNumber = mockk()
    private val observeExampleNumber: ObserveExampleNumber = mockk()
    private val observeSelectedPrinter: ObserveSelectedPrinter = mockk()
    private val observeLabelCodeType: ObserveLabelCodeType = mockk()

    override val mocks: Array<Any> = arrayOf(
        saveSelectedPrinter,
        provideBluetoothConnection,
        getBoundDevices,
        setLabelCodeType,
        setExampleNumber,
        observeExampleNumber,
        observeSelectedPrinter,
        observeLabelCodeType
    )

    private fun initService() {
        service = ConfigScreenViewModel(
            saveSelectedPrinter,
            provideBluetoothConnection,
            getBoundDevices,
            setLabelCodeType,
            setExampleNumber,
            observeExampleNumber,
            observeSelectedPrinter,
            observeLabelCodeType,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `when code type changed, setLabelCodeType use case invoked`() = runTestIn {
        every { observeExampleNumber(Unit) } returns flowOf("")
        every { observeSelectedPrinter(Unit) } returns flowOf("")
        every { observeLabelCodeType(Unit) } returns flowOf(CodeType.Barcode)
        coEvery { setLabelCodeType(any()) } just runs

        initService()

        service.changeCodeType(CodeType.QR)

        verify(exactly = 1) { observeExampleNumber(Unit) }
        verify(exactly = 1) { observeSelectedPrinter(Unit) }
        verify(exactly = 1) { observeLabelCodeType(Unit) }
        coVerify(exactly = 1) { setLabelCodeType(any()) }
    }

    @Test
    fun `when selected device changed, saveSelectedPrinter use case invoked`() = runTestIn {
        every { observeExampleNumber(Unit) } returns flowOf("")
        every { observeSelectedPrinter(Unit) } returns flowOf("")
        every { observeLabelCodeType(Unit) } returns flowOf(CodeType.Barcode)
        coEvery { saveSelectedPrinter(any()) } just runs

        initService()

        service.changeSelectedDevice(BluetoothDevice("name", "address"))

        verify(exactly = 1) { observeExampleNumber(Unit) }
        verify(exactly = 1) { observeSelectedPrinter(Unit) }
        verify(exactly = 1) { observeLabelCodeType(Unit) }
        coVerify(exactly = 1) { saveSelectedPrinter(any()) }
    }

    @Test
    fun `when example number changed, setExampleNumber use case invoked`() = runTestIn {
        every { observeExampleNumber(Unit) } returns flowOf("")
        every { observeSelectedPrinter(Unit) } returns flowOf("")
        every { observeLabelCodeType(Unit) } returns flowOf(CodeType.Barcode)
        coEvery { setExampleNumber(any()) } just runs

        initService()

        service.changeExampleNumber("number")

        verify(exactly = 1) { observeExampleNumber(Unit) }
        verify(exactly = 1) { observeSelectedPrinter(Unit) }
        verify(exactly = 1) { observeLabelCodeType(Unit) }
        coVerify(exactly = 1) { setExampleNumber(any()) }
    }

    @Test
    fun `when updateListOfBondedDevices but bluetooth error occurred, emit bluetoothError event`() =
        runTestIn {
            every { observeExampleNumber(Unit) } returns flowOf("")
            every { observeSelectedPrinter(Unit) } returns flowOf("")
            every { observeLabelCodeType(Unit) } returns flowOf(CodeType.Barcode)
            coEvery { provideBluetoothConnection(Unit) } returns Result.Error.BluetoothEnabling

            initService()

            service.events.test {
                service.updateListOfBondedDevices()
                assertEquals(
                    Event.BluetoothError,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }

            verify(exactly = 1) { observeExampleNumber(Unit) }
            verify(exactly = 1) { observeSelectedPrinter(Unit) }
            verify(exactly = 1) { observeLabelCodeType(Unit) }
            coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
        }

    @Test
    fun `when updateListOfBondedDevices failed, emit error event`() = runTestIn {
        every { observeExampleNumber(Unit) } returns flowOf("")
        every { observeSelectedPrinter(Unit) } returns flowOf("")
        every { observeLabelCodeType(Unit) } returns flowOf(CodeType.Barcode)
        coEvery { provideBluetoothConnection(Unit) } returns Result.Successful
        every { getBoundDevices(Unit) } returns DataResult.Error.BluetoothEnabling(emptyList())

        initService()

        service.events.test {
            service.updateListOfBondedDevices()
            assertInstanceOf(
                Event.Message::class.java,
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { observeExampleNumber(Unit) }
        verify(exactly = 1) { observeSelectedPrinter(Unit) }
        verify(exactly = 1) { observeLabelCodeType(Unit) }
        coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
        verify(exactly = 1) { getBoundDevices(Unit) }
    }

    @Test
    fun `when updateListOfBondedDevices succeeded, update device list`() = runTestIn {
        val listOfDevices = listOf(BluetoothDevice("name", "address"))

        every { observeExampleNumber(Unit) } returns flowOf("")
        every { observeSelectedPrinter(Unit) } returns flowOf("")
        every { observeLabelCodeType(Unit) } returns flowOf(CodeType.Barcode)
        coEvery { provideBluetoothConnection(Unit) } returns Result.Successful
        every { getBoundDevices(Unit) } returns DataResult.Successful(listOfDevices)

        initService()

        val stateFlowJob = async {
            service.state.test {
                assertEquals(
                    ConfigScreenState.InitialState(), awaitItem()
                )
                assertEquals(
                    ConfigScreenState.ReadyState(CodeType.Barcode, emptyList(), "", ""),
                    awaitItem()
                )
                assertEquals(
                    ConfigScreenState.ReadyState(CodeType.Barcode, listOfDevices, "", ""),
                    awaitItem()
                )
            }
        }

        val updateJob = async {
            service.updateListOfBondedDevices()
        }

        awaitAll(updateJob, stateFlowJob)


        verify(exactly = 1) { observeExampleNumber(Unit) }
        verify(exactly = 1) { observeSelectedPrinter(Unit) }
        verify(exactly = 1) { observeLabelCodeType(Unit) }
        coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
        verify(exactly = 1) { getBoundDevices(Unit) }
    }
}