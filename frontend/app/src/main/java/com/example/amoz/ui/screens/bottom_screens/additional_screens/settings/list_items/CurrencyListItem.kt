package com.example.amoz.ui.screens.bottom_screens.additional_screens.settings.list_items

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.CurrencyYuan
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R

@Composable
fun CurrencyListItem(currency: String, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        leadingContent = { Icon(Icons.Default.CurrencyYuan, null) },
        overlineContent = {
            Text(
                text = stringResource(R.string.current_currency),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        headlineContent = {
            Text(text = currency, style = MaterialTheme.typography.titleLarge)
        },
        trailingContent = {
            Icon(Icons.AutoMirrored.Filled.ArrowRight, null)
        }
    )
}