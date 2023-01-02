package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface GetObjectByBarcode : SuspendUseCase<String, StocktakingObject?>

class GetObjectByBarcodeImpl(private val repository: StocktakingRepository) : GetObjectByBarcode {
    override suspend fun invoke(input: String): StocktakingObject? {
        return repository.getObjectByBarcode(input)
    }
}