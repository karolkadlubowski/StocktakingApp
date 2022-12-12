package pl.polsl.stocktakingApp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.data.models.StocktakingObject

@Dao
interface StocktakingDao {
    @Upsert
    suspend fun upsert(stocktakingObject: StocktakingObject)

    @Delete
    suspend fun delete(stocktakingObject: StocktakingObject)

    @Query("SELECT * FROM stocktakingObject")
    fun observeAll(): Flow<List<StocktakingObject>>
}