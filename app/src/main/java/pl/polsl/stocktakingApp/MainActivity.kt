package pl.polsl.stocktakingApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import pl.polsl.stocktakingApp.presentation.NavGraphs
import pl.polsl.stocktakingApp.ui.theme.StocktakingAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StocktakingAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}