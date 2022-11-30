package pl.polsl.stocktakingApp.presentation.modifyObject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.presentation.common.ui.InputField
import pl.polsl.stocktakingApp.ui.theme.captionButton
import pl.polsl.stocktakingApp.ui.theme.pageTitle

@Composable
@Destination
fun ModifyObjectScreen(
    navigator: DestinationsNavigator,
    viewModel: ModifyObjectScreenViewModel = hiltViewModel(),
    objectId: String? = null,
    stocktakingObject: StocktakingObject? = null
) {
    var id = remember { mutableStateOf(TextFieldValue(objectId ?: "")) }
    var name = remember { mutableStateOf(TextFieldValue("")) }
    var description = remember { mutableStateOf(TextFieldValue("")) }
    var amount = remember { mutableStateOf(TextFieldValue("1")) }

    val screenMode: ObjectModificationMode = remember {
        if (stocktakingObject != null) ObjectModificationMode.EditMode else ObjectModificationMode.AddMode
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                    )
                )
            )
            .padding(16.dp)
    ) {

        Text(
            stringResource(id = screenMode.pageTitle),
            style = MaterialTheme.typography.pageTitle,
            modifier = Modifier
                .padding(vertical = 10.dp)
            //.weight(1f)
        )

        InputField(value = name.value, onValueChange = { name.value = it }, description = "Nazwa")
        InputField(value = id.value, onValueChange = { id.value = it }, description = "Id")
        InputField(
            value = description.value,
            onValueChange = { description.value = it },
            description = "Opis",
            singleLine = false
        )
        InputField(
            value = amount.value,
            onValueChange = { amount.value = it },
            description = "Ilość",
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {}, modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Akceptuj",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                //.height(40.dp),
                style = MaterialTheme.typography.captionButton
            )
        }

//        FilledButton(onClick = { /*TODO*/ }) {
//            Text(
//                textAlign = TextAlign.Center,
//                text = "Akceptuj",
//                modifier = Modifier
//                    .fillMaxSize(),
//                style = MaterialTheme.typography.captionButton
//            )
//        }

//        Row {
//            Button(onClick = {}) {
//                Text(text = "Akceptuj")
//            }
//
//            Button(onClick = { }) {
//                Text(text = "Anuluj")
//            }
//        }
    }
}