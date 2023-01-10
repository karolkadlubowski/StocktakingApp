package pl.polsl.stocktakingApp.domain.services

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.zebra.sdk.comm.BluetoothConnection
import com.zebra.sdk.comm.ConnectionException
import com.zebra.sdk.printer.PrinterLanguage
import com.zebra.sdk.printer.ZebraPrinter
import com.zebra.sdk.printer.ZebraPrinterFactory
import kotlinx.coroutines.delay
import pl.polsl.stocktakingApp.common.DataResult
import pl.polsl.stocktakingApp.common.Result
import pl.polsl.stocktakingApp.common.addData
import timber.log.Timber
import java.io.IOException

class BluetoothService(
    private val _context: Context
) {
    private val _bluetoothAdapter: BluetoothAdapter by lazy {
        (_context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private val isVersionAboveAndroidR: Boolean
        get() = Build.VERSION.SDK_INT > Build.VERSION_CODES.R

    private val isPermissionGranted: Boolean
        get() = ActivityCompat.checkSelfPermission(
            _context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    suspend fun provideBluetoothConnection(): Result {
        try {
            if (_bluetoothAdapter.isEnabled) {
                return Result.Successful
            } else {
                if (isVersionAboveAndroidR && !isPermissionGranted
                ) {
                    return Result.Error.PermissionNotGranted
                } else {
                    _bluetoothAdapter.enable()
                    for (i in 0 until 6) {
                        delay(300)
                        if (_bluetoothAdapter.isEnabled) {
                            return Result.Successful
                        }
                    }
                    return Result.Error.BluetoothEnabling
                }
            }
        } catch (e: Exception) {
            return Result.Error.BluetoothEnabling
        }
    }

    @SuppressLint("MissingPermission")
    fun getBondedDevices(): DataResult<List<BluetoothDevice>> {
        if (_bluetoothAdapter.isEnabled) {
            if (!isVersionAboveAndroidR || isPermissionGranted
            ) {
                return Result.Successful.addData(_bluetoothAdapter.bondedDevices.toList())
            } else {
                return Result.Error.PermissionNotGranted.addData(emptyList())
            }
        } else {
            return Result.Error.BluetoothNotEnabled.addData(emptyList())
        }
    }

    fun print(deviceAddress: String, content: String): Result {
        if (_bluetoothAdapter.isEnabled) {
            val printerConnection = BluetoothConnection(deviceAddress)
            try {
                printerConnection.open()
                val printer =
                    ZebraPrinterFactory.getInstance(PrinterLanguage.ZPL, printerConnection)
                _sendToPrint(printer!!, content)
            } catch (e: ConnectionException) {
                Timber.e(e.message)
                return Result.Error.BluetoothConnection
            } finally {
                printerConnection.close()
            }
            return Result.Successful
        } else {
            return Result.Error.BluetoothNotEnabled
        }
    }

    private fun _sendToPrint(printer: ZebraPrinter, content: String): Result {
        try {
            val fileName = "TEMP.ZPL"
            _context.deleteFile(fileName)
            val filepath = _context.getFileStreamPath(fileName)
            _createFile(fileName, content)
            printer.sendFileContents(filepath.absolutePath)
        } catch (e1: ConnectionException) {
            Timber.e(e1.message)
            return Result.Error.BluetoothConnection
        } catch (e: IOException) {
            Timber.e(e.message)
            return Result.Error.FileCreation
        }
        return Result.Successful
    }

    @Throws(IOException::class)
    private fun _createFile(fileName: String, content: String) {
        val os = _context.openFileOutput(fileName, Context.MODE_APPEND)
        os.write(content.toByteArray())
        os.flush()
        os.close()
    }
}