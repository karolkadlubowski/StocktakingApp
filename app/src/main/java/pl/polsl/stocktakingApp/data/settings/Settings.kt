package pl.polsl.stocktakingApp.data.settings


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

val SETTINGS_NAME = "preferences"

interface Settings {
    suspend fun setSelectedPrinter(deviceAddress: String)

    suspend fun getSelectedPrinter(): String?

    fun observeSelectedPrinter(): Flow<String?>

    suspend fun setLabelCodeType(codeTypeModel: CodeType)

    suspend fun getLabelCodeType(): CodeType

    fun observeLabelCodeType(): Flow<CodeType>

    suspend fun setExampleNumber(number: String)

    fun observeExampleNumber(): Flow<String?>

    suspend fun setExampleNumberRegex(regex: String)

    fun observeRegex(): Flow<String?>
}

internal class SettingsImpl(private val _dataStore: DataStore<Preferences>) : Settings {

    companion object {
        private val SELECTED_PRINTER = stringPreferencesKey("selectedPrinter")
        private val LABEL_CODE_TYPE = intPreferencesKey("labelCodeType")
        private val EXAMPLE_NUMBER = stringPreferencesKey("exampleNumber")
        private val REGEX = stringPreferencesKey("regex")
    }

    override suspend fun setSelectedPrinter(deviceAddress: String) = _dataStore.set(
        SELECTED_PRINTER,
        deviceAddress
    )

    override suspend fun getSelectedPrinter() = _dataStore.get(SELECTED_PRINTER)

    override fun observeSelectedPrinter(): Flow<String?> =
        _dataStore.data.map { it[SELECTED_PRINTER] }

    override suspend fun setLabelCodeType(codeTypeModel: CodeType) = _dataStore.set(
        LABEL_CODE_TYPE,
        codeTypeModel.ordinal
    )

    override suspend fun getLabelCodeType(): CodeType =
        CodeType.values()[_dataStore.get(LABEL_CODE_TYPE) ?: 1]

    override fun observeLabelCodeType(): Flow<CodeType> =
        _dataStore.data.map { CodeType.values()[it[LABEL_CODE_TYPE] ?: 1] }

    override suspend fun setExampleNumber(number: String) {
        val xd = _dataStore.get(REGEX)

        _dataStore.set(EXAMPLE_NUMBER, number)
    }

    override fun observeExampleNumber(): Flow<String?> = _dataStore.data.map { it[EXAMPLE_NUMBER] }

    override suspend fun setExampleNumberRegex(regex: String) =
        _dataStore.set(REGEX, regex)

    override fun observeRegex(): Flow<String?> {
        return _dataStore.data.map { it[REGEX] }
    }


    private suspend fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, value: T) {
        this.edit { it[key] = value }
    }

    private suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? =
        this.data.map { it[key] }.first()

}