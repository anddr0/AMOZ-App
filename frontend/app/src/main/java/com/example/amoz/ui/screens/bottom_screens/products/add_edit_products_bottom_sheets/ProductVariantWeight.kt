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
import com.example.amoz.api.enums.UnitWeight
import com.example.amoz.api.requests.WeightCreateRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductVariantWeight(
    weight: WeightCreateRequest?,
    onChange: (WeightCreateRequest?) -> Unit,
) {
    var weightEnabled by remember { mutableStateOf(weight != null) }
    var weightExpanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(UnitWeight.G) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, SolidColor(MaterialTheme.colorScheme.outline), RoundedCornerShape(10.dp))
    ) {
        ListItem(
            modifier = Modifier
                .clickable { weightExpanded = !weightExpanded },
            headlineContent = {
                Box(modifier = Modifier.fillMaxWidth(),) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.variant_weight),
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector =
                        if (weightExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "show product weight",
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        )

        if (weightExpanded) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.variant_weight_enable)) },
                    trailingContent = {
                        Switch(
                            checked = weightEnabled,
                            onCheckedChange = {
                                weightEnabled = it
                                if (!weightEnabled) {
                                    onChange(null)
                                }
                                else {
                                    onChange(WeightCreateRequest(selectedUnit))
                                }
                            }
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                )
                if (weightEnabled) {
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
                            UnitWeight.entries.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.name) },
                                    onClick = {
                                        selectedUnit = unit
                                        dropdownExpanded = false
                                        onChange(weight?.copy(unitWeight = unit))
                                    }
                                )
                            }
                        }
                    }

                    var textValue by remember { mutableStateOf(weight?.amount?.toString() ?: "") }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = textValue,
                        onValueChange = { newValue ->
                            textValue = newValue
                            val parsedValue = newValue.toDoubleOrNull()
                            onChange(weight?.copy(amount = parsedValue))
                        },
                        label = { Text(stringResource(R.string.variant_weight_enter_amount)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                    )

                }
            }
        }
    }
}