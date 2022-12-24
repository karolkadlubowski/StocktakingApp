package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import pl.polsl.stocktakingApp.ui.theme.C
import pl.polsl.stocktakingApp.ui.theme.inputField

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
) {
    val focusRequester = FocusRequester()

    BasicTextField(
        cursorBrush = SolidColor(Color.White),
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
            .background(Color.Transparent)
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        maxLines = 1,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = C.BorderGrey,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = C.BorderGrey,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Favorite icon",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                innerTextField()
            }
        },
        textStyle = MaterialTheme.typography.inputField,
    )
}