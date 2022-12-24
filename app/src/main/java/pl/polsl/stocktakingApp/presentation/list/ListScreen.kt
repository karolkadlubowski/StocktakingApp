package pl.polsl.stocktakingApp.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.presentation.common.Event
import pl.polsl.stocktakingApp.presentation.common.observeEvents
import pl.polsl.stocktakingApp.presentation.common.observeState
import pl.polsl.stocktakingApp.presentation.common.ui.ObjectItem
import pl.polsl.stocktakingApp.presentation.common.ui.SearchField
import pl.polsl.stocktakingApp.presentation.common.ui.SnackbarScreenWrapper
import pl.polsl.stocktakingApp.presentation.destinations.ConfigScreenDestination
import pl.polsl.stocktakingApp.presentation.destinations.ModifyObjectScreenDestination
import pl.polsl.stocktakingApp.presentation.destinations.TextScannerScreenDestination
import pl.polsl.stocktakingApp.ui.theme.C
import pl.polsl.stocktakingApp.ui.theme.D
import pl.polsl.stocktakingApp.ui.theme.pageTitle


@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun ListScreen(
    navigator: DestinationsNavigator,
    viewModel: ListScreenViewModel = hiltViewModel()
) {
    val state by viewModel.observeState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    viewModel.events.observeEvents {
        when (it) {
            is Event.NoSelectedPrinter -> {
                navigator.navigate(ConfigScreenDestination)
            }
            is Event.Message -> snackbarHostState.showSnackbar(
                it.text(context)
            )
        }
    }

    SnackbarScreenWrapper(snackbarHostState = snackbarHostState) {
        Scaffold {
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
                        stringResource(id = R.string.objectList),
                        style = MaterialTheme.typography.pageTitle,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .weight(1f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_scan),
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
                                navigator.navigate(TextScannerScreenDestination(regex = state.regex))
                            }
                            .weight(0.15f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
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
                                navigator.navigate(ModifyObjectScreenDestination())
                            }
                            .weight(0.15f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.iconDescription),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(start = D.Icon.padding)
                            .then(
                                Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    //.background(C.SettingsOrange)
                                    .background(
                                        brush = C.GradientOrange
                                    )
                            )
                            .clickable {
                                navigator.navigate(ConfigScreenDestination)
                            }
                            .weight(0.15f)
                    )

                }

                SearchField(
                    searchQuery = state.searchField,
                    onSearchQueryChanged = viewModel::changeSearchQuery
                )

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                ) {
                    if (state is ListScreenState.ReadyState) {
                        items(
                            items = (state as ListScreenState.ReadyState).list,
                            key = { it.id!! }
                        ) {
                            ObjectItem(stocktakingObject = it, onPrintButtonClicked = {
                                viewModel.printLabel(it)
                            }, onClicked = {
                                navigator.navigate(ModifyObjectScreenDestination(stocktakingObject = it))
                            })
                        }
                    }

                }
            }
        }
    }
}