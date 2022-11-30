package pl.polsl.stocktakingApp.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

typealias DataFlow<T> = Flow<StreamData<T>>

fun <T, R> DataFlow<T>.mapData(mapper: (data: T) -> R): DataFlow<R> = this.map { it.map(mapper) }

sealed class StreamData<out T> {
    abstract val data: T

    data class Ready<T>(override val data: T) : StreamData<T>()
    data class Loading<T>(override val data: T) : StreamData<T>()
}

fun <T, R> StreamData<T>.map(mapper: (data: T) -> R): StreamData<R> = when (this) {
    is StreamData.Loading -> StreamData.Loading(mapper(data))
    is StreamData.Ready -> StreamData.Ready(mapper(data))
}