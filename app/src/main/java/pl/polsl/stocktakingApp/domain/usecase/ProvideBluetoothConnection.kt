package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.common.Result
import pl.polsl.stocktakingApp.domain.SuspendUseCase
import pl.polsl.stocktakingApp.domain.services.BluetoothService

class ProvideBluetoothConnection(
    private val bluetoothService: BluetoothService
) : SuspendUseCase<Unit, Result> {
    override suspend fun invoke(input: Unit): Result =
        bluetoothService.provideBluetoothConnection()
}