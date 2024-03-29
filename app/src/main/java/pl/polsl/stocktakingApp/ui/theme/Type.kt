package pl.polsl.stocktakingApp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val Typography.captionButton: TextStyle
    @Composable
    get() = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

val Typography.pageTitle: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )

val Typography.cardTitle: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

val Typography.cardDescription: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )

val Typography.inputFieldHeader: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

val Typography.inputField: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
    )

val Typography.itemHeader: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )

val Typography.itemDescription: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )
