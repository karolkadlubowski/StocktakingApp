package pl.polsl.stocktakingApp.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.common.LoadingStatus
import pl.polsl.stocktakingApp.common.withLoading
import pl.polsl.stocktakingApp.data.dao.StocktakingDao
import pl.polsl.stocktakingApp.data.models.StocktakingObject

class StocktakingRepository(
    private val _stocktakingDao: StocktakingDao
) {
    private val loadingObjects = LoadingStatus()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeObjectList(
        queryFlow: Flow<String>
    ): DataFlow<List<StocktakingObject>> {
        return queryFlow.flatMapLatest {
            _stocktakingDao.observeAll(it).withLoading(loadingObjects)
        }
    }

    suspend fun upsertObject(stocktakingObject: StocktakingObject) {
        return _stocktakingDao.upsert(stocktakingObject)
    }

    suspend fun deleteObject(stocktakingObject: StocktakingObject) {
        _stocktakingDao.delete(stocktakingObject)
    }

    suspend fun checkIfObjectWithBarcodeExists(barcode: String): Boolean {
        return _stocktakingDao.getObjectAmountWithBarcode(barcode) != 0
    }
}