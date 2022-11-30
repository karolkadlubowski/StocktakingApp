package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pl.polsl.stocktakingApp.ui.theme.D
import pl.polsl.stocktakingApp.ui.theme.Shapes
import pl.polsl.stocktakingApp.ui.theme.emptyButton

@Composable
fun EmptyButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    onClick: (() -> Unit)?,
) {
    Surface(
//        border = BorderStroke(
//            D.EmptyButton.borderStroke,
//            color
//        ),
        shape = Shapes.rounded,
        shadowElevation = D.Elevation.default,
        modifier = modifier
            .fillMaxWidth()
            .height(D.EmptyButton.height),
    ) {
        OutlinedButton(
            onClick = onClick ?: {},
            enabled = onClick != null,
            colors = ButtonDefaults.textButtonColors(
                contentColor = color
            ),


            ) {
            Text(text = text, style = MaterialTheme.typography.emptyButton)
        }
    }
}
