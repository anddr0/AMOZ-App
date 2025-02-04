package com.example.amoz.ui.components.pickers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.ui.components.bottom_sheets.AddressBottomSheet

@Composable
fun AddressPicker(
    modifier: Modifier = Modifier,
    address: AddressCreateRequest,
    onAddressChange: (AddressCreateRequest) -> Unit,
) {
    var addressBottomSheetExpanded by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable { addressBottomSheetExpanded = true },
        leadingContent = { Icon(Icons.Outlined.Category, null) },
        overlineContent = { Text(stringResource(R.string.company_address)) },
        headlineContent = { Text(
            text = address.fullAddress.takeIf { it.isNotBlank() } ?: stringResource(R.string.company_address)
        ) },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, null)
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )

    if (addressBottomSheetExpanded) {
        AddressBottomSheet(
            bottomSheetTitle = stringResource(id = R.string.address_change_title_company),
            onDismissRequest = { addressBottomSheetExpanded = false },
            address = address,
            onDone = { onAddressChange(it) }
        )
    }

}