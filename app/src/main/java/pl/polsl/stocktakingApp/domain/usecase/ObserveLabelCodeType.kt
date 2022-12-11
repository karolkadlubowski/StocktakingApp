package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

interface ObserveLabelCodeType : UseCase<Unit, Flow<CodeType>>

internal class ObserveLabelCodeTypeImpl(private val settings: Settings) :
    ObserveLabelCodeType {
    override fun invoke(input: Unit): Flow<CodeType> = settings.observeLabelCodeType()
}