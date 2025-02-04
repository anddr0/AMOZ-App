package com.example.amoz.ui.components.list_items

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.ui.components.CustomDialogWindow
import com.example.amoz.ui.components.PrimaryFilledButton

@Composable
fun CurrentOrderCustomerListItem(
    currentCustomer: CustomerAnyRepresentation,
    modifier: Modifier = Modifier,
    onRemoveCustomer: () -> Unit,
    onSendInvoice: () -> Unit,
    isNewOrder: Boolean,
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .padding(bottom = 16.dp)
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onRemoveCustomer()
            },
        headlineContent = {
            Text(
                text = currentCustomer.nameOnInvoice
            )
        },
        supportingContent = {
            Text(
                text = currentCustomer.emailAddress ?: currentCustomer.phoneNumber,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_a_customer),
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
    if (!isNewOrder) {
        PrimaryFilledButton(
            text = stringResource(R.string.send_an_invoice),
            onClick = {
                if (currentCustomer.emailAddress != null) {
                    isDialogOpen = true
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.cant_send_invoice),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (isDialogOpen) {
        CustomDialogWindow(
            title = stringResource(R.string.send_an_invoice),
            text = stringResource(R.string.send_an_invoice_sure),
            onAccept = {
                onSendInvoice()
                isDialogOpen = false
            },
            onReject = { isDialogOpen = false },
            onDismiss = { isDialogOpen = false }
        )
    }
}