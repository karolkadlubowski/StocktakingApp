package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface DeleteObject : SuspendUseCase<DeleteObject.Params, Unit> {
    data class Params(val stocktakingObject: StocktakingObject)
}

class DeleteObjectImpl(private val repository: StocktakingRepository) : DeleteObject {
    override suspend fun invoke(input: DeleteObject.Params) {
        repository.deleteObject(input.stocktakingObject)
    }
}