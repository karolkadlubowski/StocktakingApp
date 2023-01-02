package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

class SetLabelCodeType(private val settings: Settings) :
    SuspendUseCase<CodeType, Unit> {
    override suspend fun invoke(input: CodeType) = settings.setLabelCodeType(input)
}