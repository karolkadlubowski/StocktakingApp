package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface GetSelectedPrinter : SuspendUseCase<Unit, String?>

internal class GetSelectedPrinterImpl(private val settings: Settings) :
    GetSelectedPrinter {
    override suspend fun invoke(input: Unit): String? = settings.getSelectedPrinter()
}