package pl.polsl.stocktakingApp.presentation.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import pl.polsl.stocktakingApp.ui.theme.C
import pl.polsl.stocktakingApp.ui.theme.S
import pl.polsl.stocktakingApp.ui.theme.inputField
import pl.polsl.stocktakingApp.ui.theme.inputFieldHeader

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    border: BorderStroke? = null,
    hint: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1,
    error: String? = null,
    description: String
) {

    val focusRequester = FocusRequester()

    Column(modifier.padding(bottom = 10.dp)) {
        Text(
            text = description,
            style = MaterialTheme.typography.inputFieldHeader,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Surface(
            shape = S.rounded,
            border = border,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusRequester.requestFocus()
                    onValueChange(value)
                }
        ) {
            BasicTextField(
                cursorBrush = SolidColor(Color.White),
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .background(Color.Transparent)
                    .border(
                        BorderStroke(
                            2.dp,
                            C.BorderGrey
                        )
                    )
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(10.dp),
                maxLines = maxLines,
                singleLine = singleLine,
                //placeholder = hint,
                // leadingIcon = leadingIcon,
                textStyle = MaterialTheme.typography.inputField,
                visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }
//        AnimatedVisibility(
//            visible = error != null,
//            modifier = Modifier.padding(vertical = D.Padding.inputFieldErrorLabel)
//        ) {
//            Text(
//                text = error ?: "",
//                style = inputFieldTheme.errorLabelStyle
//            )
//        }
    }
}