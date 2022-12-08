package pl.polsl.stocktakingApp.presentation.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.polsl.stocktakingApp.data.models.BluetoothDevice
import pl.polsl.stocktakingApp.ui.theme.D
import pl.polsl.stocktakingApp.ui.theme.S
import pl.polsl.stocktakingApp.ui.theme.itemDescription
import pl.polsl.stocktakingApp.ui.theme.itemHeader

@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    bluetoothDevice: BluetoothDevice,
    isChecked: Boolean,
    onCheckClick: () -> Unit,
) {
    Surface(
        shape = S.rounded,
        shadowElevation = D.Elevation.default,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier.padding(bottom = 10.dp))
    ) {
        Row(
            modifier = modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                onCheckedChange = { onCheckClick() },
                checked = isChecked
            )
            Column(
                modifier = modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = bluetoothDevice.name,
                    style = MaterialTheme.typography.itemHeader
                )
                Text(
                    text = bluetoothDevice.address,
                    style = MaterialTheme.typography.itemDescription
                )
            }
        }
    }
}