package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase

class ObserveSelectedPrinter(private val _settings: Settings) :
    UseCase<Unit, Flow<String?>> {
    override fun invoke(input: Unit): Flow<String?> = _settings.observeSelectedPrinter()
}