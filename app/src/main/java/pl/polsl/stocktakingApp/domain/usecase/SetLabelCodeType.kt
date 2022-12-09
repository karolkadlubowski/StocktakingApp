package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

interface SetLabelCodeType : SuspendUseCase<CodeType, Unit>

internal class SetLabelCodeTypeImpl(private val settings: Settings) :
    SetLabelCodeType {
    override suspend fun invoke(input: CodeType) = settings.setLabelCodeType(input)
}