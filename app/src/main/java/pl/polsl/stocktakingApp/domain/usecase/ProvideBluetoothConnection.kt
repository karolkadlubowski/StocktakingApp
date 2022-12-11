package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.printer.OutputBluetoothService
import pl.polsl.printer.Result
import pl.polsl.stocktakingApp.domain.SuspendUseCase

interface ProvideBluetoothConnection : SuspendUseCase<Unit, Result>

class ProvideBluetoothConnectionImpl(
    private val outputBluetoothService: OutputBluetoothService
) : ProvideBluetoothConnection {
    override suspend fun invoke(input: Unit): Result =
        outputBluetoothService.provideBluetoothConnection()
}