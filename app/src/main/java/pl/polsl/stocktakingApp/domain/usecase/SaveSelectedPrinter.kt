package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface SaveSelectedPrinter : SuspendUseCase<SaveSelectedPrinter.Params, Unit> {
    data class Params(val deviceAddress: String)
}

internal class SaveSelectedPrinterImpl(private val settings: Settings) :
    SaveSelectedPrinter {
    override suspend fun invoke(input: SaveSelectedPrinter.Params) =
        settings.setSelectedPrinter(input.deviceAddress)
}