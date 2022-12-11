package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.polsl.stocktakingApp.R
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.ui.theme.*

@Composable
fun ObjectItem(stocktakingObject: StocktakingObject, onPrintButtonClicked: () -> Unit) {
    Surface(
        border = BorderStroke(
            D.EmptyButton.borderStroke,
            C.BorderGrey
        ),
        shape = S.rounded,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stocktakingObject.name,
                    style = MaterialTheme.typography.cardTitle,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Text(
                    text = stocktakingObject.barcode,
                    style = MaterialTheme.typography.cardDescription
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_print),
                contentDescription = stringResource(R.string.iconDescription),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = D.Icon.padding)
//                    .then(
//                        Modifier
//                            .aspectRatio(1f)
//                            .clip(CircleShape)
//                        //.background(C.Golden)
//                    )
                    .clickable {
                        onPrintButtonClicked()
                    }
                //.weight(0.15f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ObjectItemPreview() {
    StocktakingAppTheme {
        ObjectItem(stocktakingObject = StocktakingObject(1, "Lodówka", "", 2, "BK-1053320")) {}
    }
}