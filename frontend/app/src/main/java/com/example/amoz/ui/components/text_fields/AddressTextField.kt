package com.example.amoz.ui.components.text_fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest


@Composable
fun AddressTextField(
    modifier: Modifier = Modifier,
    address: AddressCreateRequest? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    fun addressToText(address: AddressCreateRequest?): String {
        return if (address == null || address.street.isBlank() || address.city.isBlank()) {
            "" // Return empty string so the label appears in the value's place
        } else {
            listOfNotNull(
                address.street,
                address.streetNumber,
                address.apartmentNumber?.takeIf { it.isNotBlank() },
                address.postalCode,
                address.city
            )
                .filter { it.isNotBlank() }
                .joinToString(" ")
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        value = addressToText(address),
        label = { Text(stringResource(R.string.orders_address)) },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    it,
                    contentDescription = null,
                    Modifier.clickable(onClick = onClick)
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        onValueChange = {}, // Read-only field
        readOnly = true,
        maxLines = 1,
        singleLine = true,
    )
}
