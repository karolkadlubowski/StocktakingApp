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
import pl.polsl.printer.BluetoothService
import pl.polsl.printer.LabelLineDividerService
import pl.polsl.stocktakingApp.data.Database
import pl.polsl.stocktakingApp.data.dao.StocktakingDao
import pl.polsl.stocktakingApp.data.repository.StocktakingRepository
import pl.polsl.stocktakingApp.data.settings.SETTINGS_NAME
import pl.polsl.stocktakingApp.data.settings.Settings
import pl.polsl.stocktakingApp.data.settings.SettingsImpl
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
        ObserveObjectListImpl(stocktakingRepository)

    @Provides
    @Singleton
    fun providesUpsertObjectUseCase(stocktakingRepository: StocktakingRepository): UpsertObject =
        UpsertObjectImpl(stocktakingRepository)

    @Provides
    @Singleton
    fun providesBluetoothService(@ApplicationContext context: Context): BluetoothService =
        BluetoothService(context)

    @Provides
    @Singleton
    fun providesGetBondedDevicesUseCase(bluetoothService: BluetoothService): GetBondedDevices =
        GetBondedDevicesImpl(bluetoothService)

    @Provides
    @Singleton
    fun providesBluetoothConnectionUseCase(bluetoothService: BluetoothService): ProvideBluetoothConnection =
        ProvideBluetoothConnectionImpl(bluetoothService)

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
        SetLabelCodeTypeImpl(settings)

    @Provides
    @Singleton
    fun providesGetLabelCodeType(settings: Settings): GetLabelCodeType =
        GetLabelCodeTypeImpl(settings)

    @Provides
    @Singleton
    fun providesObserveLabelCodeType(settings: Settings): ObserveLabelCodeType =
        ObserveLabelCodeTypeImpl(settings)


    @Provides
    @Singleton
    fun providesGetSelectedPrinter(settings: Settings): GetSelectedPrinter =
        GetSelectedPrinterImpl(settings)

    @Provides
    @Singleton
    fun providesSaveSelectedPrinter(settings: Settings): SaveSelectedPrinter =
        SaveSelectedPrinterImpl(settings)

    @Provides
    @Singleton
    fun providesObserveSelectedPrinter(settings: Settings): ObserveSelectedPrinter =
        ObserveSelectedPrinterImpl(settings)

    @Provides
    @Singleton
    fun providesLabelLineDividerService(): LabelLineDividerService = LabelLineDividerService()

    @Provides
    @Singleton
    fun providesPrintLabel(
        bluetoothService: BluetoothService,
        labelLineDividerService: LabelLineDividerService
    ): PrintLabel = PrintLabelImpl(bluetoothService, labelLineDividerService)

    @Provides
    @Singleton
    fun providesDeleteObject(stocktakingRepository: StocktakingRepository): DeleteObject =
        DeleteObjectImpl(stocktakingRepository)

    @Provides
    @Singleton
    fun providesSetExampleNumber(settings: Settings): SetExampleNumber =
        SetExampleNumberImpl(settings)

    @Provides
    @Singleton
    fun providesObserveExampleNumber(settings: Settings): ObserveExampleNumber =
        ObserveExampleNumberImpl(settings)

    @Provides
    @Singleton
    fun providesObserveRegex(settings: Settings): ObserveRegex = ObserveRegexImpl(settings)

    @Provides
    @Singleton
    fun providesSetRegex(settings: Settings, regexService: RegexService): SetRegex =
        SetRegexImpl(settings, regexService)

    @Provides
    @Singleton
    fun providesRegexService(): RegexService = RegexService()
}