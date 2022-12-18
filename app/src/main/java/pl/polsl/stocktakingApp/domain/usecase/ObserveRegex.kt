package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.domain.UseCase

interface ObserveRegex : UseCase<Unit, Flow<String?>>

class ObserveRegexImpl(private val settings: Settings) : ObserveRegex {
    override fun invoke(input: Unit): Flow<String?> = settings.observeRegex()
}