package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.SuspendUseCase

class DeleteObject(private val _repository: StocktakingRepository) :
    SuspendUseCase<StocktakingObject, Unit> {
    override suspend fun invoke(input: StocktakingObject) {
        _repository.deleteObject(input)
    }
}