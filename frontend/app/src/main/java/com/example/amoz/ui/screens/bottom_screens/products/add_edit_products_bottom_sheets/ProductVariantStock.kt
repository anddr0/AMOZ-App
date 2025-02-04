package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.requests.StockCreateRequest
import com.example.amoz.ui.HorizontalDividerWithText

@Composable
fun ProductVariantStock(
    stockCreateRequest: StockCreateRequest,
    onChange: (StockCreateRequest) -> Unit,
) {
    var stockViewExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, SolidColor(MaterialTheme.colorScheme.outline), RoundedCornerShape(10.dp))
    ) {
        ListItem(
            modifier = Modifier
                .clickable { stockViewExpanded = !stockViewExpanded },
            headlineContent = {
                Box(modifier = Modifier.fillMaxWidth(),) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.variant_stock),
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector =
                        if (stockViewExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "show product stock",
                    )
                }

            },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        )

        if (stockViewExpanded) {
            Spacer(Modifier.height(10.dp))
            // -------------------- Stock Amount --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                value = stockCreateRequest.amountAvailable?.toString() ?: "",
                onValueChange = { newValue ->
                    onChange(
                        stockCreateRequest.copy(
                            amountAvailable =
                            newValue.toIntOrNull()?.let { if (it >= 0) it else 0 }
                        )
                    )
                },
                label = {Text(stringResource(R.string.variant_stock_amount))},
                leadingIcon = {
                    IconButton(onClick = {
                        if (stockCreateRequest.amountAvailable != null && stockCreateRequest.amountAvailable > 0) {
                            onChange(
                                stockCreateRequest.copy(
                                    amountAvailable = stockCreateRequest.amountAvailable.minus(1)
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        onChange(
                            stockCreateRequest.copy(
                                amountAvailable = stockCreateRequest.amountAvailable?.plus(1) ?: 0
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    }
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "0",
                        textAlign = TextAlign.Center
                    )
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                ),
            )

            // -------------------- Alarming Amount --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                value = stockCreateRequest.alarmingAmount?.toString() ?: "",
                onValueChange = { newValue ->
                    onChange(
                        stockCreateRequest.copy(
                            alarmingAmount =
                            newValue.toIntOrNull()?.let { if (it >= 0) it else 0 }
                        )
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        if (stockCreateRequest.amountAvailable != null && stockCreateRequest.amountAvailable > 0) {
                            onChange(
                                stockCreateRequest.copy(
                                    alarmingAmount = stockCreateRequest.alarmingAmount?.minus(1)
                                        ?: 0
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = null
                        )
                    }
                },
                label = { Text(stringResource(R.string.variant_stock_alarming_state)) },
                trailingIcon = {
                    IconButton(onClick = {
                        onChange(
                            stockCreateRequest.copy(
                                alarmingAmount = stockCreateRequest.alarmingAmount?.plus(1) ?: 0
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    }
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "0",
                        textAlign = TextAlign.Center
                    )
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                )
            )
        }
    }
}