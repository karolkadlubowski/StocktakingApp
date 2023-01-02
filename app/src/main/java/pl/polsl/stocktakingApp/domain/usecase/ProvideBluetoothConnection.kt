package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.common.Result
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.domain.services.BluetoothService

interface ProvideBluetoothConnection : SuspendUseCase<Unit, Result>

class ProvideBluetoothConnectionImpl(
    private val bluetoothService: BluetoothService
) : ProvideBluetoothConnection {
    override suspend fun invoke(input: Unit): Result =
        bluetoothService.provideBluetoothConnection()
}