package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

class GetLabelCodeType(private val _settings: Settings) :
    SuspendUseCase<Unit, CodeType> {
    override suspend fun invoke(input: Unit): CodeType = _settings.getLabelCodeType()
}