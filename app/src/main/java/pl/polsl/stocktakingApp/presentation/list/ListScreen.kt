package pl.polsl.stocktakingApp.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.presentation.destinations.ConfigScreenDestination
import pl.polsl.stocktakingApp.presentation.destinations.TextScannerScreenDestination


@Destination(start = true)
@Composable
fun ListScreen(
    navigator: DestinationsNavigator,
    viewModel: ListScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("List Screen")

        Button(onClick = {
            navigator.navigate(ConfigScreenDestination)
        }) {
            Text(text = "Go to Config Screen")
        }

        Button(onClick = {
            navigator.navigate(TextScannerScreenDestination)
        }) {
            Text(text = "Go to Scanner Screen")
        }

    }
}