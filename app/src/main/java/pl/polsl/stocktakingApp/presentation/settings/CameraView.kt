//package pl.polsl.stocktakingApp.presentation.settings
//
//import android.content.Context
//import androidx.camera.core.AspectRatio
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.Box
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalInspectionMode
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//
//
//@Composable
//fun CameraView(
//    modifier: Modifier,
//    imageCapture: ImageCapture,
//) {
//    if (!LocalInspectionMode.current) {
//        val context = LocalContext.current
//        val lifecycleOwner = LocalLifecycleOwner.current
//
//        val previewView = remember { PreviewView(context) }
//        val preview = remember {
//            Preview.Builder()
//                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                .build()
//        }
//        val cameraSelector = remember {
//            CameraSelector.Builder()
//                .requireLensFacing(
//                    CameraSelector.LENS_FACING_BACK
//                )
//                .build()
//        }
//
//        LaunchedEffect(key1 = "asd") {
//            val cameraProvider = context.getCameraProvider()
//
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                lifecycleOwner,
//                cameraSelector,
//                preview,
//                imageCapture
//            )
//
//            preview.setSurfaceProvider(previewView.surfaceProvider)
//        }
//
//        AndroidView(
//            { previewView },
//            modifier = modifier,
//        )
//    } else {
//        Box(modifier)
//    }
//}
//
//suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
//    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//        cameraProvider.addListener({
//            continuation.resume(cameraProvider.get())
//        }, ContextCompat.getMainExecutor(this))
//    }
//}