package pl.polsl.stocktakingApp.presentation.configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.polsl.stocktakingApp.common.observeState
import pl.polsl.stocktakingApp.presentation.common.ui.FilterSwitcher
import pl.polsl.stocktakingApp.presentation.common.ui.InputField
import pl.polsl.stocktakingApp.ui.theme.inputFieldHeader
import pl.polsl.stocktakingApp.ui.theme.pageTitle

@Destination
@Composable
fun ConfigScreen(
    navigator: DestinationsNavigator,
    viewModel: ConfigScreenViewModel = hiltViewModel()
) {
    val state by viewModel.observeState()

    LaunchedEffect(key1 = "initBluetooth") {
        viewModel.updateListOfBondedDevices()
    }

    var regex = remember { mutableStateOf(TextFieldValue("")) }

    LazyColumn(
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
        item {
            Text(
                "Konfiguracja",
                style = MaterialTheme.typography.pageTitle,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            InputField(
                value = regex.value,
                onValueChange = { regex.value = it },
                description = "Przykładowy numer"
            )

            Text(
                text = "Rodzaj etykiety",
                style = MaterialTheme.typography.inputFieldHeader,
                modifier = Modifier.padding(
                    bottom = 6.dp,
                    top = 10.dp
                )
            )

            FilterSwitcher(
                //modifier = Modifier.padding(horizontal = D.Padding.rippleSmall),
                selectedTabIndex = state.codeType.ordinal,
                tabs = CodeType.values()
                    .map { stringResource(id = it.stringId) },
            ) {
                viewModel.changeCodeType(CodeType.values()[it])
            }

            Text(
                text = "Urządzenie bluetooth",
                style = MaterialTheme.typography.inputFieldHeader,
                modifier = Modifier.padding(
                    bottom = 6.dp,
                    top = 10.dp
                )
            )
        }

        items(
            items = state.bluetoothDeviceList,
            key = { it.address }
        ) {
            DeviceItem(bluetoothDevice = it, isChecked = false, onCheckClick = {
                viewModel.changeSelectedDevice(it)
            })
        }
    }
}

//@Composable
//fun ImageSelectorAndCropper(
//    state: ConfigScreenState,
//    changeUri: (uri: Uri?) -> Unit
//) {
////    var imageUri: Uri? = remember {
////        null
////    }
//    val context = LocalContext.current
//    val bitmap = remember {
//        mutableStateOf<Bitmap?>(null)
//    }
//
//    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
//        if (result.isSuccessful) {
//            // use the cropped image
//            //imageUri = result.uriContent
//            changeUri(result.uriContent)
//        } else {
//            // an error occurred cropping
//            val exception = result.error
//        }
//    }
//
//    val imagePickerLauncher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
//            imageCropLauncher.launch(cropOptions)
//        }
//
//    if (state.uri != null) {
//        Image(
//            painter = rememberImagePainter(
//                data = state.uri
//            ),
//            contentDescription = null,
//            modifier = Modifier.wrapContentSize(),
//            contentScale = ContentScale.FillBounds
//        )
//
//        if (Build.VERSION.SDK_INT < 28) {
//            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, state.uri)
//        } else {
//            val source = ImageDecoder.createSource(context.contentResolver, state.uri!!)
//            bitmap.value = ImageDecoder.decodeBitmap(source)
//        }
//    } else {
//        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
//            Text("Pick image to crop")
//        }
//    }
//}
