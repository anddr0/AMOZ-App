package com.example.amoz.ui.components.dropdown_menus

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
import com.example.amoz.api.enums.Sex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexDropdownMenu(
    selectedSex: Sex,
    onSexChange: (Sex) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val sexOptions = Sex.entries.associateWith { it.getName() }

    ExposedDropdownMenuBox(
        expanded = dropDownMenuExpanded,
        onExpandedChange = { dropDownMenuExpanded = !dropDownMenuExpanded }
    ) {
        OutlinedTextField(
            value = selectedSex.getName(),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.profile_sex)) },
            trailingIcon = {
                Icon(
                    imageVector = if (dropDownMenuExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            modifier = modifier
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
            sexOptions.forEach { (sex, name) ->
                DropdownMenuItem(
                    text = { Text(text = name) },
                    onClick = {
                        onSexChange(sex)
                        dropDownMenuExpanded = false
                    }
                )
            }
        }
    }
}
