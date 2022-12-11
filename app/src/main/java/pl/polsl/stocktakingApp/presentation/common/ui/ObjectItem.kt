package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.ui.theme.*

@Composable
fun ObjectItem(stocktakingObject: StocktakingObject) {
    Surface(
        border = BorderStroke(
            D.EmptyButton.borderStroke,
            C.BorderGrey
        ),
        shape = S.rounded,
        modifier = Modifier
            .fillMaxWidth()
            .background(C.Transparent)
            .padding(bottom = 10.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                text = stocktakingObject.name,
                style = MaterialTheme.typography.cardTitle,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Text(text = stocktakingObject.barcode, style = MaterialTheme.typography.cardDescription)

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ObjectItemPreview() {
    StocktakingAppTheme {
        ObjectItem(stocktakingObject = StocktakingObject(1, "Lodówka", "", 2, "BK-1053320"))
    }
}