package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.polsl.stocktakingApp.ui.theme.C
import pl.polsl.stocktakingApp.ui.theme.StocktakingAppTheme

@Composable
fun BottomBar(
    /**
     * we should set to true when we want the bottombar to be dummy
     * to take space so that the content above it could take maximum space
     * with the content showing near the cornets
     * */
    isPlaceholder: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(C.CardBlack)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 38.dp
                )
        ) {

//            Divider(
//                color = C.LightGray,
//                modifier = Modifier
//                    .padding(
//                        top = D.Padding.bottomBarDividerTop,
//                        bottom = D.Padding.bottomBarDividerBottom
//                    )
//                    .size(
//                        width = D.Size.Width.bottomBarDivider,
//                        height = D.Size.Thickness.bottomBarDivider
//                    )
//
//                    .align(Alignment.CenterHorizontally)
//            )
            content()
        }
    }
}

@Composable
fun ButtonBottomBar(
    buttonText: String,
    isPlaceholder: Boolean = false,
    onClickButton: (() -> Unit)?
) {
    BottomBar(
        isPlaceholder = isPlaceholder,
    ) {
        EmptyButton(
            text = buttonText,
            //elevation = D.Elevation.default,
            onClick = onClickButton,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    StocktakingAppTheme {
        BottomBar {
            Text("Test")
        }
    }
}

@Preview
@Composable
private fun ButtonBottomBarPreview() {
    StocktakingAppTheme {
        ButtonBottomBar(buttonText = "Kliknij mnie") {
        }
    }
}