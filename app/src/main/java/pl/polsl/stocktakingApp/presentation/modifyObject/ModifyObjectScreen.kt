package pl.polsl.stocktakingApp.presentation.modifyObject

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.presentation.common.ui.InputField
import pl.polsl.stocktakingApp.presentation.destinations.ListScreenDestination
import pl.polsl.stocktakingApp.ui.theme.D
import pl.polsl.stocktakingApp.ui.theme.captionButton
import pl.polsl.stocktakingApp.ui.theme.pageTitle

@Composable
@Destination
fun ModifyObjectScreen(
    navigator: DestinationsNavigator,
    viewModel: ModifyObjectScreenViewModel = hiltViewModel(),
    code: String? = null,
    stocktakingObject: StocktakingObject? = null
) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val barcode = remember { mutableStateOf(TextFieldValue("")) }
    val description =
        remember { mutableStateOf(TextFieldValue("")) }
    val amount =
        remember { mutableStateOf(TextFieldValue("1")) }

    LaunchedEffect(key1 = "init", block = {
        viewModel.init(stocktakingObject)
        if (stocktakingObject != null) {
            name.value = TextFieldValue(stocktakingObject.name)
            barcode.value = TextFieldValue(stocktakingObject.barcode)
            description.value = TextFieldValue(stocktakingObject.description)
            amount.value = TextFieldValue(stocktakingObject.amount.toString())
        } else if (code != null) {
            barcode.value = TextFieldValue(code)
            viewModel.setBarcode(code)
        }
    })

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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(id = screenMode.pageTitle),
                style = MaterialTheme.typography.pageTitle,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = stringResource(R.string.iconDescription),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = D.Icon.padding)
                    .then(
                        Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                        //.background(C.Golden)
                    )
                    .clickable {
                        viewModel.deleteObject()
                        navigator.popBackStack()
                    }
                    .weight(0.15f)
            )
        }

        InputField(
            value = name.value,
            onValueChange = {
                name.value = it
                viewModel.setName(it.text)
            },
            description = "Nazwa"
        )
        InputField(
            value = barcode.value,
            onValueChange = {
                barcode.value = it
                viewModel.setBarcode(it.text)

            },
            description = "Kod"
        )
        InputField(
            value = description.value,
            onValueChange = {
                description.value = it
                viewModel.setDescription(it.text)

            },
            description = "Opis",
            singleLine = false
        )
        InputField(
            value = amount.value,
            onValueChange = {
                amount.value = it
                viewModel.setAmount(it.text.toInt())
            },
            description = "Ilość",
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                viewModel.upsertObject()
                navigator.popBackStack(ListScreenDestination, false)
            }, modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Akceptuj",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.captionButton
            )
        }
    }
}