package pl.polsl.stocktakingApp.presentation.modifyObject

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.presentation.common.Event
import pl.polsl.stocktakingApp.presentation.common.observeEvents
import pl.polsl.stocktakingApp.presentation.common.observeState
import pl.polsl.stocktakingApp.presentation.common.ui.InputField
import pl.polsl.stocktakingApp.presentation.common.ui.SnackbarScreenWrapper
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
    val state by viewModel.observeState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    viewModel.events.observeEvents {
        when (it) {
            is ModifyObjectScreenViewModel.ObjectUpsertSuccess -> {
                navigator.popBackStack(ListScreenDestination, false)
            }
            is Event.Message -> snackbarHostState.showSnackbar(
                it.text(context)
            )
        }
    }

    LaunchedEffect(key1 = "init", block = {
        if (code != null) {
            viewModel.init(StocktakingObject(barcode = code))
        }

        viewModel.init(stocktakingObject)
    })

    val screenMode: ObjectModificationMode = remember {
        if (stocktakingObject != null) ObjectModificationMode.EditMode else ObjectModificationMode.AddMode
    }

    SnackbarScreenWrapper(snackbarHostState = snackbarHostState) {
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
                if (state is ModifyObjectScreenState.EditObjectState) {
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
                            )
                            .clickable {
                                viewModel.deleteObject()
                                navigator.popBackStack()
                            }
                            .weight(0.15f)
                    )
                }
            }

            InputField(
                value = state.name,
                onValueChange = viewModel::setName,
                description = "Nazwa"
            )
            InputField(
                value = state.barcode,
                onValueChange = viewModel::setBarcode,
                description = "Kod",
                isEnabled = state is ModifyObjectScreenState.AddObjectState
            )
            InputField(
                value = state.description,
                onValueChange = viewModel::setDescription,
                description = "Opis",
                singleLine = false
            )
            InputField(
                value = state.amount,
                onValueChange = { viewModel.setAmount(it.filterNot { it.isWhitespace() || !it.isDigit() }) },
                description = "Ilość",
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    viewModel.upsertObject()
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
}