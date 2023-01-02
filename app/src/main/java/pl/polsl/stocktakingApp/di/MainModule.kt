package pl.polsl.stocktakingApp.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import pl.polsl.stocktakingApp.data.Database
import pl.polsl.stocktakingApp.data.dao.StocktakingDao
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.data.settings.SETTINGS_NAME
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.data.settings.SettingsImpl
import pl.polsl.stocktakingApp.domain.services.BluetoothService
import pl.polsl.stocktakingApp.domain.services.LabelLineDividerService
import pl.polsl.stocktakingApp.domain.services.RegexService
import pl.polsl.stocktakingApp.domain.usecase.*
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
        ObserveObjectList(stocktakingRepository)

    @Provides
    @Singleton
    fun providesUpsertObjectUseCase(stocktakingRepository: StocktakingRepository): UpsertObject =
        UpsertObject(stocktakingRepository)

    @Provides
    @Singleton
    fun providesBluetoothService(@ApplicationContext context: Context): BluetoothService =
        BluetoothService(context)

    @Provides
    @Singleton
    fun providesGetBondedDevicesUseCase(bluetoothService: BluetoothService): GetBoundDevices =
        GetBoundDevices(bluetoothService)

    @Provides
    @Singleton
    fun providesBluetoothConnectionUseCase(bluetoothService: BluetoothService): ProvideBluetoothConnection =
        ProvideBluetoothConnection(bluetoothService)

    @Singleton
    @Provides
    fun providesSettings(@ApplicationContext appContext: Context): Settings = SettingsImpl(
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(
                SharedPreferencesMigration(
                    appContext,
                    SETTINGS_NAME
                )
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(SETTINGS_NAME) },
        ),
    )

    @Provides
    @Singleton
    fun providesSetLabelCodeType(settings: Settings): SetLabelCodeType =
        SetLabelCodeType(settings)

    @Provides
    @Singleton
    fun providesGetLabelCodeType(settings: Settings): GetLabelCodeType =
        GetLabelCodeType(settings)

    @Provides
    @Singleton
    fun providesObserveLabelCodeType(settings: Settings): ObserveLabelCodeType =
        ObserveLabelCodeType(settings)


    @Provides
    @Singleton
    fun providesGetSelectedPrinter(settings: Settings): GetSelectedPrinter =
        GetSelectedPrinter(settings)

    @Provides
    @Singleton
    fun providesSaveSelectedPrinter(settings: Settings): SaveSelectedPrinter =
        SaveSelectedPrinter(settings)

    @Provides
    @Singleton
    fun providesObserveSelectedPrinter(settings: Settings): ObserveSelectedPrinter =
        ObserveSelectedPrinter(settings)

    @Provides
    @Singleton
    fun providesLabelLineDividerService(): LabelLineDividerService = LabelLineDividerService()

    @Provides
    @Singleton
    fun providesPrintLabel(
        bluetoothService: BluetoothService,
        labelLineDividerService: LabelLineDividerService
    ): PrintLabel = PrintLabel(bluetoothService, labelLineDividerService)

    @Provides
    @Singleton
    fun providesDeleteObject(stocktakingRepository: StocktakingRepository): DeleteObject =
        DeleteObject(stocktakingRepository)

    @Provides
    @Singleton
    fun providesSetExampleNumber(settings: Settings): SetExampleNumber =
        SetExampleNumber(settings)

    @Provides
    @Singleton
    fun providesObserveExampleNumber(settings: Settings): ObserveExampleNumber =
        ObserveExampleNumber(settings)

    @Provides
    @Singleton
    fun providesRegexService(): RegexService = RegexService()

    @Provides
    @Singleton
    fun providesCheckIfObjectExists(stocktakingRepository: StocktakingRepository): GetObjectByBarcode =
        GetObjectByBarcode(stocktakingRepository)
}