package com.example.amoz.ui.components.pickers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.pickers.CustomerPicker

@Composable
fun CustomerPickerListItem(
    modifier: Modifier = Modifier,
    onCustomerChange: (CustomerAnyRepresentation) -> Unit,
    onSaveState: () -> Unit,
    navController: NavController
) {
    val customerPicker = CustomerPicker(navController)
    val selectedCustomer = customerPicker.getPickedCustomer()

    LaunchedEffect(selectedCustomer) {
        if (selectedCustomer != null) {
            onCustomerChange(selectedCustomer)
            customerPicker.removePickedCustomer()
        }
    }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onSaveState()
                customerPicker.navigateToCustomerScreen()
            })
            .padding(start = 10.dp)
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.choose_a_customer),
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.choose_a_customer),
        )
    }
}