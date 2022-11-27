package pl.polsl.stocktakingApp.presentation.textScanner

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.canhub.cropper.CropImageContract
import com.google.mlkit.vision.common.InputImage

@Composable
fun ImageCropper(onImageCropped: (inputImage: InputImage, uri: Uri) -> Unit, context: Context) =
    rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            onImageCropped(
                InputImage.fromFilePath(context, result.uriContent!!),
                result.uriContent!!
            )
        } else {
            val exception = result.error
        }
    }
