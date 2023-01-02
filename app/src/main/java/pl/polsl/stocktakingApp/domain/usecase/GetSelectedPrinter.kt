package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

class GetSelectedPrinter(private val settings: Settings) :
    SuspendUseCase<Unit, String?> {
    override suspend fun invoke(input: Unit): String? = settings.getSelectedPrinter()
}