package pl.polsl.stocktakingApp.domain.usecase

import kotlinx.coroutines.flow.flowOf
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.UseCase

interface ObserveObjectList : UseCase<Unit, DataFlow<List<StocktakingObject>>>

class ObserveObjectListImpl(private val repository: StocktakingRepository) : ObserveObjectList {
    override fun invoke(input: Unit): DataFlow<List<StocktakingObject>> {
        return repository.observeObjectList(flowOf(""))
    }
}