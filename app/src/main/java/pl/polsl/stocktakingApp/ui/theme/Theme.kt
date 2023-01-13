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

private val DarkColorScheme = darkColorScheme(
    primary = C.BackgroundDarker,
    secondary = C.BackgroundLighter,
    tertiary = C.BackgroundDarker,
    surface = C.BackgroundLighter,
)

private val LightColorScheme = lightColorScheme(
    primary = C.BackgroundDarker,
    secondary = C.BackgroundLighter,
    tertiary = C.BackgroundDarker,
    surface = C.BackgroundLighter,
)

@Composable
fun StocktakingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
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
        itemShape = S.rounded,
        elevation = 12.dp
    )
}