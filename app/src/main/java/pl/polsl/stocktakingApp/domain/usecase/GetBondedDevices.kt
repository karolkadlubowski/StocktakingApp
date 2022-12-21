package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.printer.BluetoothService
import pl.polsl.printer.DataResult
import pl.polsl.printer.mapData
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.domain.UseCase

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