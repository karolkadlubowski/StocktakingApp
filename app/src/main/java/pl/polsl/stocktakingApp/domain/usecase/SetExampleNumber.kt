package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface SetExampleNumber : SuspendUseCase<String, Unit>

class SetExampleNumberImpl(private val settings: Settings) : SetExampleNumber {
    override suspend fun invoke(input: String) = settings.setExampleNumber(number = input)
}