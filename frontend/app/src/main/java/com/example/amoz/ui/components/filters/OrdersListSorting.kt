package com.example.amoz.ui.components.filters

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.amoz.R
import com.example.amoz.view_models.OrdersViewModel
import com.example.amoz.view_models.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListSorting(
    sortingType: OrdersViewModel.SortingType,
    onSortingTypeChange: (OrdersViewModel.SortingType) -> Unit,
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val sortingOptions = mapOf(
        OrdersViewModel.SortingType.ASCENDING_PRICE to R.string.sorting_type_ascending_price,
        OrdersViewModel.SortingType.DESCENDING_PRICE to R.string.sorting_type_descending_price,
        OrdersViewModel.SortingType.ASCENDING_TIME_OF_CREATION to R.string.sorting_type_ascending_time_of_creation,
        OrdersViewModel.SortingType.DESCENDING_TIME_OF_CREATION to R.string.sorting_type_descending_time_of_creation,
        OrdersViewModel.SortingType.ASCENDING_TIME_OF_SENDING to R.string.sorting_type_ascending_time_of_sending,
        OrdersViewModel.SortingType.DESCENDING_TIME_OF_SENDING to R.string.sorting_type_descending_time_of_sending,
        OrdersViewModel.SortingType.NONE to R.string.sorting_type_none
    )

    ExposedDropdownMenuBox(
        expanded = dropDownMenuExpanded,
        onExpandedChange = { dropDownMenuExpanded = !dropDownMenuExpanded }
    ) {
        OutlinedTextField(
            value = stringResource(sortingOptions[sortingType] ?: R.string.sorting_type_none),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.sorting_type)) },
            trailingIcon = {
                Icon(
                    imageVector = if (dropDownMenuExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )

        ExposedDropdownMenu(
            expanded = dropDownMenuExpanded,
            onDismissRequest = { dropDownMenuExpanded = false }
        ) {
            sortingOptions.forEach { (type, labelResId) ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = labelResId)) },
                    onClick = {
                        onSortingTypeChange(type)
                        dropDownMenuExpanded = false
                    }
                )
            }
        }
    }
}