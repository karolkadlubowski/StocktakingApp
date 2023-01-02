package pl.polsl.stocktakingApp.screen

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.polsl.stocktakingApp.base.BaseTest
import pl.polsl.stocktakingApp.common.StreamData
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.usecase.*
import pl.polsl.stocktakingApp.presentation.common.Event
import pl.polsl.stocktakingApp.presentation.configuration.CodeType
import pl.polsl.stocktakingApp.presentation.list.ListScreenState
import pl.polsl.stocktakingApp.presentation.list.ListScreenViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class ListScreenViewModelTest : BaseTest<ListScreenViewModel>() {
    private val printLabel: PrintLabel = mockk()
    private val getLabelCodeType: GetLabelCodeType = mockk()
    private val getSelectedPrinter: GetSelectedPrinter = mockk()
    private val observeRegex: ObserveExampleNumber = mockk()
    private val observeObjectList: ObserveObjectList = mockk()
    private val provideBluetoothConnection: ProvideBluetoothConnection = mockk()

    override val mocks: Array<Any> = arrayOf(
        printLabel, getLabelCodeType, getSelectedPrinter, observeObjectList, observeRegex
    )

    private fun initService() {
        service = ListScreenViewModel(
            printLabel,
            getLabelCodeType,
            getSelectedPrinter,
            provideBluetoothConnection,
            observeRegex,
            observeObjectList,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `when search query changed, update viewmodel state`() = runTestIn {
        val query = "query"

        every { observeObjectList.invoke(any()) } returns flow {
            delay(10)
            emit(StreamData.Loading(emptyList()))
            delay(10)
            emit(StreamData.Ready(listOf()))
        }
        every { observeRegex.invoke(Unit) } returns flowOf("")

        initService()

        service.changeSearchQuery(query)

        service.state.test {
            assertEquals(
                ListScreenState.InitialState,
                awaitItem()
            )
            assertEquals(
                ListScreenState.ReadyState(listOf(), "", query),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { observeObjectList.invoke(any()) }
        verify(exactly = 1) { observeRegex.invoke(Unit) }
    }

    @Test
    fun `when print invoked but printer not selected, emit NoSelectedPrinter event`() = runTestIn {

        every { observeObjectList.invoke(any()) } returns flowOf(StreamData.Ready(listOf()))
        every { observeRegex.invoke(Unit) } returns flowOf("")
        coEvery { getSelectedPrinter(Unit) } returns null
        coEvery { provideBluetoothConnection(Unit) } returns pl.polsl.printer.Result.Successful

        initService()

        service.events.test {
            service.printLabel(stocktakingObject = stocktakingObject)
            assertEquals(
                Event.NoSelectedPrinter,
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { observeObjectList.invoke(any()) }
        verify(exactly = 1) { observeRegex.invoke(Unit) }
        coVerify(exactly = 1) { getSelectedPrinter(Unit) }
        coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
    }

    @Test
    fun `when print invoked and printer selected, print label`() = runTestIn {
        val printerAddress = "printerAddress"
        val labelType = CodeType.Barcode

        every { observeObjectList.invoke(any()) } returns flowOf(StreamData.Ready(listOf()))
        every { observeRegex.invoke(Unit) } returns flowOf("")
        coEvery { getSelectedPrinter(Unit) } returns printerAddress
        coEvery { getLabelCodeType(Unit) } returns labelType
        coEvery { provideBluetoothConnection(Unit) } returns pl.polsl.printer.Result.Successful
        every { printLabel(any()) } returns pl.polsl.printer.Result.Successful

        initService()

        service.printLabel(stocktakingObject = stocktakingObject)

        verify(exactly = 1) { observeObjectList.invoke(any()) }
        verify(exactly = 1) { observeRegex.invoke(Unit) }
        coVerify(exactly = 1) { getSelectedPrinter(Unit) }
        coVerify(exactly = 1) { getLabelCodeType(Unit) }
        coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
        coVerify(exactly = 1) { printLabel(any()) }
    }

    @Test
    fun `when print invoked and printer selected but print action failed, emit error event`() =
        runTestIn {
            val printerAddress = "printerAddress"
            val labelType = CodeType.Barcode

            every { observeObjectList.invoke(any()) } returns flowOf(StreamData.Ready(listOf()))
            every { observeRegex.invoke(Unit) } returns flowOf("")
            coEvery { getSelectedPrinter(Unit) } returns printerAddress
            coEvery { getLabelCodeType(Unit) } returns labelType
            coEvery { provideBluetoothConnection(Unit) } returns pl.polsl.printer.Result.Successful
            every { printLabel(any()) } returns pl.polsl.printer.Result.Error.BluetoothConnection

            initService()
            service.events.test {
                service.printLabel(stocktakingObject = stocktakingObject)
                assertEquals(
                    ListScreenViewModel.PrintLabelError,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }

            verify(exactly = 1) { observeObjectList.invoke(any()) }
            verify(exactly = 1) { observeRegex.invoke(Unit) }
            coVerify(exactly = 1) { getSelectedPrinter(Unit) }
            coVerify(exactly = 1) { getLabelCodeType(Unit) }
            coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
            coVerify(exactly = 1) { printLabel(any()) }
        }

    @Test
    fun `when print invoked but bluetooth provider returns error, emit error event`() =
        runTestIn {

            every { observeObjectList.invoke(any()) } returns flowOf(StreamData.Ready(listOf()))
            every { observeRegex.invoke(Unit) } returns flowOf("")
            coEvery { provideBluetoothConnection(Unit) } returns pl.polsl.printer.Result.Error.BluetoothEnabling

            initService()
            service.events.test {
                service.printLabel(stocktakingObject = stocktakingObject)
                assertEquals(
                    Event.BluetoothError,
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }

            verify(exactly = 1) { observeObjectList.invoke(any()) }
            verify(exactly = 1) { observeRegex.invoke(Unit) }
            coVerify(exactly = 1) { provideBluetoothConnection(Unit) }
        }

    companion object {
        private val stocktakingObject = StocktakingObject()
    }
}