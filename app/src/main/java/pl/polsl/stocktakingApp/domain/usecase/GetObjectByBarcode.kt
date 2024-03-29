package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.SuspendUseCase

class GetObjectByBarcode(private val _repository: StocktakingRepository) :
    SuspendUseCase<String, StocktakingObject?> {
    override suspend fun invoke(input: String): StocktakingObject? {
        return _repository.getObjectByBarcode(input)
    }
}