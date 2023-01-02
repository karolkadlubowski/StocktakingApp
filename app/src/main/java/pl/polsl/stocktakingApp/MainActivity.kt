package pl.polsl.stocktakingApp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
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
                requestForegroundPermission(this@MainActivity)
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }

    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST = 34
    private fun foregroundPermissionApproved(context: Context): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        )
    }

    private fun requestForegroundPermission(context: Context) {
        val provideRationale = foregroundPermissionApproved(context)

        if (provideRationale) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
            )
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
            )
        }
    }
}