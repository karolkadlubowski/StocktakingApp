package pl.polsl.stocktakingApp.presentation.common

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow


@Composable
internal fun <STATE> BaseViewModel<STATE>.observeState(
    initialState: STATE = this.initialState,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<STATE> = this.state.observeWithLifecycle(
    initialState = initialState,
    lifecycleState = lifecycleState,
)

@Composable
internal fun <TYPE> Flow<TYPE>.observeWithLifecycle(
    initialState: TYPE,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<TYPE> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(
        this,
        lifecycleOwner
    ) {
        this.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            lifecycleState
        )
    }
    return flowLifecycleAware.collectAsState(initial = initialState)
}

@SuppressLint("ComposableNaming")
@Composable
internal fun <T> Flow<T>.observeEvents(
    block: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(
        this,
        lifecycleOwner
    ) {
        this.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    LaunchedEffect(key1 = flowLifecycleAware) {
        flowLifecycleAware
            .collect {
                block(it)
            }
    }
}