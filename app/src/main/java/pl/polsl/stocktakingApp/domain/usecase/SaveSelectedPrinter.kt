package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

class SaveSelectedPrinter(private val _settings: Settings) :
    SuspendUseCase<String, Unit> {
    override suspend fun invoke(input: String) =
        _settings.setSelectedPrinter(input)
}