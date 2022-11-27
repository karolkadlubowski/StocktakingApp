package pl.polsl.stocktakingApp.presentation.configuration

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import pl.polsl.stocktakingApp.presentation.common.BaseViewModel

@Composable
internal fun <STATE> BaseViewModel<STATE>.observeState(
    initialState: STATE = this.initialState,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<STATE> = this.state.observeWithLifecycle(
    initialState = initialState,
    lifecycleState = lifecycleState,
)

@Composable
internal fun <TYPE> Flow<TYPE>.observeWithLifecycle(
    initialState: TYPE,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<TYPE> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(
        this,
        lifecycleOwner
    ) {
        this.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            lifecycleState
        )
    }
    return flowLifecycleAware.collectAsState(initial = initialState)
}


@Destination
@Composable
fun ConfigScreen(
    navigator: DestinationsNavigator,
    viewModel: ConfigScreenViewModel = hiltViewModel()
) {
    val state by viewModel.observeState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Scan Screen")

        ImageSelectorAndCropper(state, viewModel::changeUri)
    }
}

@Composable
fun ImageSelectorAndCropper(
    state: ConfigScreenState,
    changeUri: (uri: Uri?) -> Unit
) {
//    var imageUri: Uri? = remember {
//        null
//    }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            //imageUri = result.uriContent
            changeUri(result.uriContent)
        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
            imageCropLauncher.launch(cropOptions)
        }

    if (state.uri != null) {
        Image(
            painter = rememberImagePainter(
                data = state.uri
            ),
            contentDescription = null,
            modifier = Modifier.wrapContentSize(),
            contentScale = ContentScale.FillBounds
        )

        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, state.uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, state.uri!!)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
    } else {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Pick image to crop")
        }
    }
}
