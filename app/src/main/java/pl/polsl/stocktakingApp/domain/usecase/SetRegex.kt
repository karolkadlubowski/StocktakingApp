package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface SetRegex : SuspendUseCase<String, Unit>

class SetRegexImpl(private val settings: Settings) : SetRegex {
    override suspend fun invoke(input: String) = settings.setExampleNumberRegex(regex = input)
}