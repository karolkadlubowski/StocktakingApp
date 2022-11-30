package pl.polsl.stocktakingApp.data

import androidx.room.RoomDatabase
import pl.polsl.stocktakingApp.data.dao.StocktakingDao
import pl.polsl.stocktakingApp.data.models.StocktakingObject

@androidx.room.Database(
    entities = [StocktakingObject::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun stocktakingDao(): StocktakingDao
}