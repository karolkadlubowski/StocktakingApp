package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

class ObserveLabelCodeType(private val _settings: Settings) :
    UseCase<Unit, Flow<CodeType>> {
    override fun invoke(input: Unit): Flow<CodeType> = _settings.observeLabelCodeType()
}