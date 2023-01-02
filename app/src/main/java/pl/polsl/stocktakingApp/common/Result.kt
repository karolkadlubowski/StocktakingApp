package pl.polsl.stocktakingApp.common

/**
 * If there is some case that we would like to return subclass of Result of DataResult from the same method,
 * this will help, BUT WE SHOULD NOT DO THAT, we should return Result or DataResult
 * */
interface BaseResult

/**
 * Result defines the possible outcomes of performing operation.
 *
 * If there should be more subclasses we can add them, but there are two cases:
 *      1. If it is specific for given operation, we should create the subclass under the /result directory
 *      2. When it is used in many different operations it can be added here
 * */
sealed class Result : BaseResult {
    object Successful : Result()

    sealed class Error : Result() {
        object NoInternetConnection : Error()
        object Server : Error()
        object Unauthorized : Error()
        object Internal : Error()
        object Unknown : Error()
        object NotFound : Error()
        object IncorrectServerData : Error()
        object BluetoothConnection : Error()
        object FileCreation : Error()
        object PermissionNotGranted : Error()
        object BluetoothNotEnabled : Error()
        object BluetoothEnabling : Error()
        object RequirePasswordChange : Error()

        data class Surplus(val remarks: String?) : Error()

        data class Group(val errors: List<Error>) : Error()
    }

    operator fun plus(other: Result): Result = when (this) {
        Successful -> other
        is Error.Group -> when (other) {
            is Error.Group -> Error.Group(this.errors + other.errors)
            Successful -> this
            is Error -> Error.Group(this.errors + other)
        }
        is Error -> when (other) {
            Successful -> this
            is Error.Group -> Error.Group(other.errors + this)
            else -> Error.Group(
                listOf(
                    this,
                    other
                ) as List<Error>
            )
        }
    }

}

/**
 * DataResult defines the possible outcomes of performing operation with data.
 *
 * If there should be more subclasses we can add them, but there are two cases:
 *      1. If it is specific for given operation, we should create the subclass under the /result directory
 *      2. When it is used in many different operations it can be added here
 * */
sealed class DataResult<out T> : BaseResult {
    abstract val data: T

    data class Successful<out T>(override val data: T) : DataResult<T>()

    sealed class Error<T> : DataResult<T>() {
        data class NoInternetConnection<T>(override val data: T) : Error<T>()
        data class Server<T>(override val data: T) : Error<T>()
        data class NotAuthorized<T>(override val data: T) : Error<T>()
        data class Internal<T>(override val data: T) : Error<T>()
        data class Unknown<T>(override val data: T) : Error<T>()
        data class NotFound<T>(override val data: T) : Error<T>()
        data class BluetoothConnection<T>(override val data: T) : Error<T>()
        data class FileCreation<T>(override val data: T) : Error<T>()
        data class PermissionNotGranted<T>(override val data: T) : Error<T>()
        data class BluetoothNotEnabled<T>(override val data: T) : Error<T>()
        data class BluetoothEnabling<T>(override val data: T) : Error<T>()
        data class IncorrectServerData<T>(override val data: T) : Error<T>()
        data class Surplus<T>(val remarks: String?, override val data: T) : Error<T>()
        data class Group<T>(override val data: T, val errors: List<Result.Error>) : Error<T>()
        data class RequirePasswordChange<T>(override val data: T) : Error<T>()
    }
}

fun <T> DataResult<T>.eraseData(): Result = when (this) {
    is DataResult.Error.NoInternetConnection -> Result.Error.NoInternetConnection
    is DataResult.Error.NotAuthorized -> Result.Error.Unauthorized
    is DataResult.Error.Server -> Result.Error.Server
    is DataResult.Error.Internal -> Result.Error.Internal
    is DataResult.Successful -> Result.Successful
    is DataResult.Error.Group -> Result.Error.Group(errors)
    is DataResult.Error.Unknown -> Result.Error.Unknown
    is DataResult.Error.NotFound -> Result.Error.NotFound
    is DataResult.Error.IncorrectServerData -> Result.Error.IncorrectServerData
    is DataResult.Error.BluetoothConnection -> Result.Error.BluetoothConnection
    is DataResult.Error.FileCreation -> Result.Error.FileCreation
    is DataResult.Error.PermissionNotGranted -> Result.Error.PermissionNotGranted
    is DataResult.Error.BluetoothNotEnabled -> Result.Error.BluetoothNotEnabled
    is DataResult.Error.BluetoothEnabling -> Result.Error.BluetoothEnabling
    is DataResult.Error.Surplus -> Result.Error.Surplus(remarks)
    is DataResult.Error.RequirePasswordChange -> Result.Error.RequirePasswordChange
}

fun <T> Result.addData(data: T): DataResult<T> = when (this) {
    Result.Error.NoInternetConnection -> DataResult.Error.NoInternetConnection(data)
    Result.Error.Unauthorized -> DataResult.Error.NotAuthorized(data)
    Result.Error.Server -> DataResult.Error.Server(data)
    Result.Error.Internal -> DataResult.Error.Internal(data)
    Result.Successful -> DataResult.Successful(data)
    Result.Error.Unknown -> DataResult.Error.Unknown(data)
    Result.Error.NotFound -> DataResult.Error.NotFound(data)
    is Result.Error.Group -> DataResult.Error.Group(
        data,
        errors
    )
    Result.Error.IncorrectServerData -> DataResult.Error.IncorrectServerData(data)
    Result.Error.BluetoothConnection -> DataResult.Error.BluetoothConnection(data)
    Result.Error.FileCreation -> DataResult.Error.FileCreation(data)
    Result.Error.PermissionNotGranted -> DataResult.Error.PermissionNotGranted(data)
    Result.Error.BluetoothNotEnabled -> DataResult.Error.BluetoothNotEnabled(data)
    Result.Error.BluetoothEnabling -> DataResult.Error.BluetoothEnabling(data)
    is Result.Error.Surplus -> TODO()
    Result.Error.RequirePasswordChange -> DataResult.Error.RequirePasswordChange(data)
}

fun <T, R> DataResult<T>.mapData(mapper: (T) -> R): DataResult<R> = when (this) {
    is DataResult.Error.NoInternetConnection -> DataResult.Error.NoInternetConnection(mapper(data))
    is DataResult.Error.NotAuthorized -> DataResult.Error.NotAuthorized(mapper(data))
    is DataResult.Error.Server -> DataResult.Error.Server(mapper(data))
    is DataResult.Error.Internal -> DataResult.Error.Internal(mapper(data))
    is DataResult.Successful -> DataResult.Successful(mapper(data))
    is DataResult.Error.Unknown -> DataResult.Error.Unknown(mapper(data))
    is DataResult.Error.Group -> DataResult.Error.Group(
        mapper(data),
        errors
    )
    is DataResult.Error.NotFound -> DataResult.Error.NotFound(mapper(data))
    is DataResult.Error.IncorrectServerData -> DataResult.Error.IncorrectServerData(mapper(data))
    is DataResult.Error.BluetoothConnection -> DataResult.Error.BluetoothConnection(mapper(data))
    is DataResult.Error.FileCreation -> DataResult.Error.FileCreation(mapper(data))
    is DataResult.Error.PermissionNotGranted -> DataResult.Error.PermissionNotGranted(mapper(data))
    is DataResult.Error.BluetoothNotEnabled -> DataResult.Error.BluetoothNotEnabled(mapper(data))
    is DataResult.Error.BluetoothEnabling -> DataResult.Error.BluetoothEnabling(mapper(data))
    is DataResult.Error.Surplus -> DataResult.Error.Surplus(
        remarks,
        mapper(data)
    )
    is DataResult.Error.RequirePasswordChange -> DataResult.Error.RequirePasswordChange(mapper(data))
}