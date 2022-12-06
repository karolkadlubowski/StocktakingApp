package pl.polsl.stocktakingApp.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel


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