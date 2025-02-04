package com.example.amoz.ui.components.text_fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.example.amoz.R
import java.math.BigDecimal

@Composable
fun PriceTextField(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    currency: String? = null,
    price: BigDecimal?,
    onPriceChange: (BigDecimal?) -> Unit,
) {
    var priceText by remember { mutableStateOf(price?.toString() ?: "") }
    var priceValue by remember { mutableStateOf(BigDecimal.ZERO) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = priceText,
        onValueChange = { input ->
            if (input.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                priceText = input
                onPriceChange(input.toBigDecimalOrNull())
            }
        },
        placeholder = { Text("0.00") },
        label = {
            Text (
                text = label,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                )
            }
        },
        trailingIcon = currency?.let {
            {
            Text(text = stringResource(R.string.currency))
                }
        },
        maxLines = 1,
        singleLine = true
    )
}