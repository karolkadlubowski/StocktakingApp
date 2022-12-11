package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

interface GetLabelCodeType : SuspendUseCase<Unit, CodeType>

internal class GetLabelCodeTypeImpl(private val settings: Settings) :
    GetLabelCodeType {
    override suspend fun invoke(input: Unit): CodeType = settings.getLabelCodeType()
}