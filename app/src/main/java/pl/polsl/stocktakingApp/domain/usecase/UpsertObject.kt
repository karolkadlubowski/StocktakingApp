package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface UpsertObject : SuspendUseCase<StocktakingObject, Unit>

class UpsertObjectImpl(private val repository: StocktakingRepository) : UpsertObject {
    override suspend fun invoke(input: StocktakingObject) {
        return repository.upsertObject(input)
    }
}