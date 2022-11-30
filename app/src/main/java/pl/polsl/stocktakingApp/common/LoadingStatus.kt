package pl.polsl.stocktakingApp.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onSubscription
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal class LoadingStatus {
    private var _loadingChannel: MutableSharedFlow<Boolean>? = null
    private var _isLoading: Boolean = false
    val isLoading: Boolean
        get() = _isLoading

    suspend fun setLoading(isLoading: Boolean) {
        _isLoading = isLoading
        _loadingChannel?.emit(_isLoading)
    }

    fun consume(): Flow<Boolean> {
        if (_loadingChannel == null) {
            _loadingChannel = MutableSharedFlow(replay = 1)
        }

        return _loadingChannel!!.onSubscription {
            this.emit(_isLoading)
        }
    }

}

@OptIn(ExperimentalContracts::class)
internal suspend fun <T> LoadingStatus.loadingScope(scope: suspend () -> T): T {
    contract {
        callsInPlace(
            scope,
            InvocationKind.EXACTLY_ONCE
        )
    }
    this.setLoading(true)
    val result = scope()
    this.setLoading(false)
    return result
}


@JvmName("withLoadingSimpleData")
internal fun <T> Flow<T>.withLoading(loadingStatus: LoadingStatus): DataFlow<T> =
    this.combine(loadingStatus.consume()) { data, loading ->
        when {
            loading -> StreamData.Loading(data)
            else -> StreamData.Ready(data)
        }
    }

@JvmName("withLoadingDataStream")
internal fun <T> DataFlow<T>.withLoading(loadingStatus: LoadingStatus): DataFlow<T> =
    this.combine(loadingStatus.consume()) { data, loading ->
        when {
            loading -> StreamData.Loading(data.data)
            else -> StreamData.Ready(data.data)
        }
    }