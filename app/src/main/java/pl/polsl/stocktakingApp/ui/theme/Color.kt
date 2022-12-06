package pl.polsl.stocktakingApp.ui.theme

import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

object C {
    val LightBlue = Color(0xFF5DBAFE)
    val AlmostBlack = Color(0xFF1E1E1F)
    val BackgroundDarker = Color(0xFF1C2023)
    val BackgroundLighter = Color(0xFF32393F)
    val CardBlack = Color(0xFF2D3134)
    val BorderGrey = Color(0xFF45494C)
    val Transparent = Color(0)
    val SettingsOrange = Color(0xFFF7941C)
    val xd1 = Color(0xffF7B05C)
    val xd2 = Color(0xFFF6931A)
    val statusBarColor =
        BackgroundDarker.copy(alpha = 0.08f).compositeOver(BackgroundLighter.copy())

    val GradientOrange = linearGradient(
        colors = listOf(
            C.xd1,
            C.xd2,
        )
    )


}

