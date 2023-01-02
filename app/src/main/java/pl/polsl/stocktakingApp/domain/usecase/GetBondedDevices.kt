package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.stocktakingApp.common.DataResult
import pl.polsl.stocktakingApp.common.mapData
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.domain.UseCase
import pl.polsl.stocktakingApp.domain.services.BluetoothService

interface GetBondedDevices : UseCase<Unit, DataResult<List<BluetoothDevice>>>

class GetBondedDevicesImpl(
    private val bluetoothService: BluetoothService
) : GetBondedDevices {
    override fun invoke(input: Unit): DataResult<List<BluetoothDevice>> {
        return bluetoothService.getBondedDevices().mapData { list ->
            list.map { BluetoothDevice(it.name, it.address) }
        }
    }
}