package pl.polsl.stocktakingApp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat

//private val textFieldColors: TextFieldColors
//@Composable
//    get() = TextFieldDefaults.textFieldColors()

private val DarkColorScheme = darkColorScheme(
    primary = C.BackgroundDarker,
    secondary = C.BackgroundLighter,
    tertiary = C.BackgroundDarker,
//    //background = Color.White,
//    onBackground = C.AlmostBlack,
    surface = C.BackgroundLighter,
//    onSurface = C.AlmostBlack,
)

private val LightColorScheme = lightColorScheme(
    primary = C.BackgroundDarker,
    secondary = C.BackgroundLighter,
    tertiary = C.BackgroundDarker,
//    //background = Color.White,
//    onBackground = C.AlmostBlack,
    surface = C.BackgroundLighter,
//    onSurface = C.AlmostBlack,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun StocktakingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val systemUiController = rememberSystemUiController()
//    if(darkTheme){
//        systemUiController.setSystemBarsColor(
//            color = DarkColorScheme.background,
//            darkIcons = true,
//        )
//    }else{
//        systemUiController.setSystemBarsColor(
//            color = LightColorScheme.background,
//            darkIcons = true,
//            )
//    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
            (view.context as Activity).window.navigationBarColor = C.statusBarColor.toArgb()
            //colorScheme.primary.toArgb()
//                colorScheme.primary.copy(alpha = 0.08f).compositeOver(colorScheme.surface.copy())
//                    .toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Immutable
data class FilterSwitcherTheme(
    val indicatorColor: Brush,
    val selectedTextColor: Color,
    val textColor: Color,
    val selectedFontWeight: FontWeight,
    val fontWeight: FontWeight,
    val fontSize: TextUnit,
    val itemShape: CornerBasedShape,
    val elevation: Dp
)

val LocalFilterSwitcherTheme = staticCompositionLocalOf {
    FilterSwitcherTheme(
        indicatorColor = C.GradientOrange,
        selectedTextColor = Color.White,
        textColor = Color.White,
        selectedFontWeight = FontWeight.W700,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        itemShape = Shape.rounded,
        elevation = 12.dp
    )
}