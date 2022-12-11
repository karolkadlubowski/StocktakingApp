package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase

interface ObserveSelectedPrinter : UseCase<Unit, Flow<String?>>

internal class ObserveSelectedPrinterImpl(private val settings: Settings) :
    ObserveSelectedPrinter {
    override fun invoke(input: Unit): Flow<String?> = settings.observeSelectedPrinter()
}