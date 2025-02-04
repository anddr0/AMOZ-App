package com.example.amoz.ui.components.bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.ErrorText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityBottomSheet(
    stock: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var quantityText by remember { mutableStateOf("1") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val invalidQuantityRangeMessage = stringResource(R.string.invalid_quantity_range, 1, stock)

    ModalBottomSheet(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.enter_quantity),
                    style = MaterialTheme.typography.displaySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (quantity > 1)
                                {
                                    quantity -= 1
                                    quantityText = quantity.toString()
                                    errorMessage = null
                                } else {
                                    errorMessage = invalidQuantityRangeMessage
                                }
                            },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = stringResource(R.string.decrease_quantity)
                            )
                        }

                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { newValue ->
                                if (newValue.isEmpty()) {
                                    quantity = 0
                                    quantityText = ""
                                    errorMessage = null
                                } else {
                                    val input = newValue.toIntOrNull()
                                    if (input != null && input > 0) {
                                        quantity = input
                                        quantityText = input.toString()
                                        errorMessage = null
                                    } else {
                                        errorMessage = invalidQuantityRangeMessage
                                    }
                                }
                            },
                            label = { Text(stringResource(R.string.quantity)) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(120.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                            singleLine = true,
                            maxLines = 1,
                        )

                        IconButton(
                            onClick = {
                                if (quantity < stock) {
                                    quantity += 1
                                    quantityText = quantity.toString()
                                    errorMessage = null
                                } else {
                                    errorMessage = invalidQuantityRangeMessage
                                }
                            },
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.increase_quantity)
                            )
                        }
                    }

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorText(errorMessage = it)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (quantity in 1..stock) {
                            onConfirm(quantity)
                        } else {
                            errorMessage = invalidQuantityRangeMessage
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.confirm))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        },
        onDismissRequest = { onDismiss() }
    )
}
