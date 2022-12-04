package pl.polsl.stocktakingApp.data.repository

import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.common.DataFlow
import pl.polsl.stocktakingApp.common.LoadingStatus
import pl.polsl.stocktakingApp.common.withLoading
import pl.polsl.stocktakingApp.data.dao.StocktakingDao
import pl.polsl.stocktakingApp.data.models.StocktakingObject

class StocktakingRepository(
    private val _stocktakingDao: StocktakingDao
) {
    private val loadingObjects = LoadingStatus()

    fun observeObjectList(
        queryFlow: Flow<String>
    ): DataFlow<List<StocktakingObject>> {
        return _stocktakingDao.observeAll().withLoading(loadingObjects)
    }

    suspend fun upsertObject(stocktakingObject: StocktakingObject) {
        return _stocktakingDao.upsert(stocktakingObject)
    }
}