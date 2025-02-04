package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.enums.UnitDimensions
import com.example.amoz.api.requests.DimensionsCreateRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductVariantDimensions(
    dimensions: DimensionsCreateRequest?,
    onChange: (DimensionsCreateRequest?) -> Unit,
) {
    var dimensionsEnabled by remember { mutableStateOf(dimensions != null) }
    var dimensionsExpanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(UnitDimensions.CM) }

    var height by remember { mutableStateOf(dimensions?.height?.toString() ?: "") }
    var width by remember { mutableStateOf(dimensions?.width?.toString() ?: "") }
    var length by remember { mutableStateOf(dimensions?.length?.toString() ?: "") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, SolidColor(MaterialTheme.colorScheme.outline), RoundedCornerShape(10.dp))
    ) {
        ListItem(
            modifier = Modifier
                .clickable { dimensionsExpanded = !dimensionsExpanded },
            headlineContent = {
                Box(modifier = Modifier.fillMaxWidth(),) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.variant_dimensions),
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector =
                        if (dimensionsExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "show product dimensions",
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        )

        if (dimensionsExpanded) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.variant_dimensions_enable)) },
                    trailingContent = {
                        Switch(
                            checked = dimensionsEnabled,
                            onCheckedChange = {
                                dimensionsEnabled = it
                                if (!dimensionsEnabled) {
                                    onChange(null)
                                } else {
                                    onChange(DimensionsCreateRequest(unitDimensions = selectedUnit))
                                }
                            }
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                )

                if (dimensionsEnabled) {
                    var dropdownExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedUnit.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.variant_weight_select_unit)) },
                            trailingIcon = {
                                Icon(
                                    imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            UnitDimensions.entries.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.name) },
                                    onClick = {
                                        selectedUnit = unit
                                        dropdownExpanded = false
                                        onChange(dimensions?.copy(unitDimensions = unit))
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = height,
                        onValueChange = { newValue ->
                            height = newValue
                            val parsedValue = newValue.toDoubleOrNull()
                            onChange(dimensions?.copy(height = parsedValue))
                        },
                        label = { Text(stringResource(R.string.variant_dimensions_height)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = width,
                        onValueChange = { newValue ->
                            width = newValue
                            val parsedValue = newValue.toDoubleOrNull()
                            onChange(dimensions?.copy(width = parsedValue))
                        },
                        label = { Text(stringResource(R.string.variant_dimensions_width)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = length,
                        onValueChange = { newValue ->
                            length = newValue
                            val parsedValue = newValue.toDoubleOrNull()
                            onChange(dimensions?.copy(length = parsedValue))
                        },
                        label = { Text(stringResource(R.string.variant_dimensions_length)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        )
                    )

                }
            }
        }
    }
}