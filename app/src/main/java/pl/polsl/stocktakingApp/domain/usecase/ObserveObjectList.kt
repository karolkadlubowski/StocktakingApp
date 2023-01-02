package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.UseCase

class ObserveObjectList(private val repository: StocktakingRepository) :
    UseCase<Flow<String>, DataFlow<List<StocktakingObject>>> {
    override fun invoke(input: Flow<String>): DataFlow<List<StocktakingObject>> {
        return repository.observeObjectList(input)
    }
}