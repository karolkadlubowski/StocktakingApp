package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface CheckIfObjectExists : SuspendUseCase<String, Boolean>

class CheckIfObjectExistsImpl(private val repository: StocktakingRepository) : CheckIfObjectExists {
    override suspend fun invoke(input: String): Boolean {
        return repository.checkIfObjectWithBarcodeExists(input)
    }
}