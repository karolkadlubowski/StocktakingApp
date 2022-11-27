package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddObjectDialog(objectId: String?, onAccept: () -> Unit, onCancel: () -> Unit) {
    Dialog(
        onDismissRequest = onCancel, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        val shape = RoundedCornerShape(10.dp)

        var id = remember { mutableStateOf(TextFieldValue(objectId ?: "")) }
        var name = remember { mutableStateOf(TextFieldValue("")) }
        var description = remember { mutableStateOf(TextFieldValue("")) }
        var amount = remember { mutableStateOf(TextFieldValue("1")) }

        Surface(
            shape = shape,
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    //.background(color)
                    .clip(shape),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Dodaj obiekt")
                BasicTextField(
                    value = id.value, onValueChange = {
                        id.value = it
                    }, enabled = objectId.isNullOrBlank(),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                BasicTextField(
                    value = name.value, onValueChange = {
                        name.value = it
                    },
                    modifier = Modifier.padding(bottom = 10.dp),
                    singleLine = true
                )
                BasicTextField(
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    },
                    modifier = Modifier.padding(bottom = 10.dp),
                )
                BasicTextField(
                    value = amount.value, onValueChange = {
                        amount.value = it
                    },
                    modifier = Modifier.padding(bottom = 10.dp),
                    singleLine = true
                )

                Row {
                    Button(onClick = onAccept) {
                        Text(text = "Akceptuj")
                    }

                    Button(onClick = onCancel) {
                        Text(text = "Anuluj")
                    }
                }
            }
        }
    }
}