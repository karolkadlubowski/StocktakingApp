package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase

interface ObserveExampleNumber : UseCase<Unit, Flow<String?>>

class ObserveExampleNumberImpl(private val settings: Settings) : ObserveExampleNumber {
    override fun invoke(input: Unit): Flow<String?> = settings.observeExampleNumber()
}