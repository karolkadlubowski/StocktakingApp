package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.domain.services.RegexService

interface SetRegex : SuspendUseCase<String, Unit>

class SetRegexImpl(private val settings: Settings, private val regexService: RegexService) :
    SetRegex {
    override suspend fun invoke(input: String) {
        settings.setExampleNumberRegex(regex = regexService.rewriteStringToRegex(input))
    }
}