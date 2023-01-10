package pl.polsl.stocktakingApp.presentation.configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import pl.polsl.stocktakingApp.presentation.common.observeState
import pl.polsl.stocktakingApp.presentation.common.ui.FilterSwitcher
import pl.polsl.stocktakingApp.presentation.common.ui.InputField
import pl.polsl.stocktakingApp.ui.theme.inputFieldHeader
import pl.polsl.stocktakingApp.ui.theme.pageTitle

@Destination
@Composable
fun ConfigScreen(
    viewModel: ConfigScreenViewModel = hiltViewModel()
) {
    val state by viewModel.observeState()

    LaunchedEffect(key1 = "initBluetooth") {
        viewModel.updateListOfBondedDevices()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                    )
                )
            )
            .padding(16.dp)
    ) {
        item {
            Text(
                "Konfiguracja",
                style = MaterialTheme.typography.pageTitle,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            InputField(
                value = state.exampleNumber ?: "",
                onValueChange = {
                    viewModel.changeExampleNumber(it)
                },
                description = "Przykładowy numer"
            )

            Text(
                text = "Rodzaj etykiety",
                style = MaterialTheme.typography.inputFieldHeader,
                modifier = Modifier.padding(
                    bottom = 6.dp,
                    top = 10.dp
                )
            )

            FilterSwitcher(
                selectedTabIndex = state.codeType.ordinal,
                tabs = CodeType.values()
                    .map { stringResource(id = it.stringId) },
            ) {
                viewModel.changeCodeType(CodeType.values()[it])
            }

            Text(
                text = "Urządzenie bluetooth",
                style = MaterialTheme.typography.inputFieldHeader,
                modifier = Modifier.padding(
                    bottom = 6.dp,
                    top = 10.dp
                )
            )
        }

        items(
            items = state.bluetoothDeviceList,
            key = { it.address }
        ) {
            DeviceItem(
                bluetoothDevice = it,
                isChecked = state.selectedPrinterAddress == it.address,
                onCheckClick = {
                    viewModel.changeSelectedDevice(it)
                })
        }
    }
}
