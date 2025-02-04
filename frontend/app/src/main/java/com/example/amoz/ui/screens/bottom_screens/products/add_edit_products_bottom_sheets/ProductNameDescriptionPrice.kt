package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.text_fields.PriceTextField
import java.math.BigDecimal

@Composable
fun ProductNameDescriptionPrice(
    productName: String?,
    showProductDescription: Boolean = false,
    productDescription: String? = null,
    productPrice: BigDecimal?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: ((String) -> Unit)? = null,
    onPriceChange: (BigDecimal?) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // -------------------- Product's name --------------------
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.product_name)) },
            value = productName ?: "",
            onValueChange = { onNameChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            singleLine = true
        )

        // -------------------- Product's description --------------------
        if(showProductDescription) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = productDescription ?: "",
                onValueChange = { onDescriptionChange?.invoke(it) },
                label = { Text(stringResource(R.string.product_description)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
        }

        // -------------------- Product's price --------------------
        PriceTextField(
            label = stringResource(R.string.product_price),
            price = productPrice,
            onPriceChange = { onPriceChange(it) }
        )
    }
}