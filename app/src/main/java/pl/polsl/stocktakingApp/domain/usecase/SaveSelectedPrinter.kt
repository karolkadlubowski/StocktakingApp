package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface SaveSelectedPrinter : SuspendUseCase<String, Unit>

internal class SaveSelectedPrinterImpl(private val settings: Settings) :
    SaveSelectedPrinter {
    override suspend fun invoke(input: String) =
        settings.setSelectedPrinter(input)
}