package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SnackbarScreenWrapper(snackbarHostState: SnackbarHostState, content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        content()

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}