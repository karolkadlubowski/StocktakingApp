package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.printer.BluetoothService
import pl.polsl.printer.Result
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface ProvideBluetoothConnection : SuspendUseCase<Unit, Result>

class ProvideBluetoothConnectionImpl(
    private val bluetoothService: BluetoothService
) : ProvideBluetoothConnection {
    override suspend fun invoke(input: Unit): Result =
        bluetoothService.provideBluetoothConnection()
}