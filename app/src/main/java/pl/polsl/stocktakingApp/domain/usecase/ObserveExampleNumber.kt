package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase

class ObserveExampleNumber(private val settings: Settings) : UseCase<Unit, Flow<String?>> {
    override fun invoke(input: Unit): Flow<String?> = settings.observeExampleNumber()
}