package pl.polsl.stocktakingApp.screen

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.polsl.stocktakingApp.base.BaseTest
import pl.polsl.stocktakingApp.domain.services.RegexService
import pl.polsl.stocktakingApp.domain.usecase.GetObjectByBarcode
import pl.polsl.stocktakingApp.domain.usecase.ObserveExampleNumber
import pl.polsl.stocktakingApp.presentation.textScanner.TextScannerScreenState
import pl.polsl.stocktakingApp.presentation.textScanner.TextScannerScreenViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class TextScannerViewModelTest : BaseTest<TextScannerScreenViewModel>() {
    private val regexService: RegexService = mockk()
    private val getObjectByBarcode: GetObjectByBarcode = mockk()
    private val observeRegex: ObserveExampleNumber = mockk()

    override val mocks: Array<Any> = arrayOf(
        regexService,
        getObjectByBarcode,
        observeRegex
    )

    private fun initService() {
        service = TextScannerScreenViewModel(
            regexService,
            getObjectByBarcode,
            observeRegex,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `when number found, change state to Found`() = runTestIn {
        val number = "number"
        every { observeRegex(Unit) } returns flowOf("")

        initService()

        val updateJob = async {
            service.onNumberFound(number)
        }

        val assertJob = async {
            service.state.test {
                assertEquals(TextScannerScreenState.Scanning, awaitItem())
                assertEquals(TextScannerScreenState.Found(number), awaitItem())
            }
        }

        awaitAll(
            updateJob,
            assertJob
        )

        verify(exactly = 1) { observeRegex(Unit) }
    }

    @Test
    fun `when continue scanning invoked, change state to Scanning`() = runTestIn {
        val number = "number"
        every { observeRegex(Unit) } returns flowOf("")

        initService()

        service.onNumberFound(number)

        val updateJob = async {
            delay(2000)
            service.onNumberFound(number)
            delay(2000)
            service.continueScanning()
        }

        val assertJob = async {
            service.state.test {
                assertEquals(TextScannerScreenState.Scanning, awaitItem())
                assertEquals(TextScannerScreenState.Found(number), awaitItem())
                assertEquals(TextScannerScreenState.Scanning, awaitItem())
            }
        }

        awaitAll(
            updateJob,
            assertJob
        )

        verify(exactly = 1) { observeRegex(Unit) }
    }
}