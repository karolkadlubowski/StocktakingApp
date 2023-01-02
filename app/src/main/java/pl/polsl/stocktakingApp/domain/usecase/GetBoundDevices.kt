package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.common.DataResult
import pl.polsl.stocktakingApp.common.mapData
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.domain.UseCase
import pl.polsl.stocktakingApp.domain.services.BluetoothService

interface GetBoundDevices : UseCase<Unit, DataResult<List<BluetoothDevice>>>

class GetBoundDevicesImpl(
    private val bluetoothService: BluetoothService
) : GetBoundDevices {
    override fun invoke(input: Unit): DataResult<List<BluetoothDevice>> {
        return bluetoothService.getBondedDevices().mapData { list ->
            list.map { BluetoothDevice(it.name, it.address) }
        }
    }
}