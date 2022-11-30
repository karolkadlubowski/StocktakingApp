package pl.polsl.stocktakingApp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import pl.polsl.stocktakingApp.data.Database
import pl.polsl.stocktakingApp.data.dao.StocktakingDao
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.domain.usecase.ObserveObjectList
import pl.polsl.stocktakingApp.domain.usecase.ObserveObjectListImpl
import pl.polsl.stocktakingApp.domain.usecase.UpsertObject
import pl.polsl.stocktakingApp.domain.usecase.UpsertObjectImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        "stocktaking_db"
    ).build()

    @Provides
    @Singleton
    fun providesStocktakingDao(database: Database): StocktakingDao = database.stocktakingDao()

    @Provides
    @Singleton
    fun providesStocktakingRepository(stocktakingDao: StocktakingDao): StocktakingRepository =
        StocktakingRepository(stocktakingDao)

    @Provides
    @Singleton
    fun providesObserveObjectListUseCase(stocktakingRepository: StocktakingRepository): ObserveObjectList =
        ObserveObjectListImpl(stocktakingRepository)

    @Provides
    @Singleton
    fun providesUpsertObjectUseCase(stocktakingRepository: StocktakingRepository): UpsertObject =
        UpsertObjectImpl(stocktakingRepository)
}