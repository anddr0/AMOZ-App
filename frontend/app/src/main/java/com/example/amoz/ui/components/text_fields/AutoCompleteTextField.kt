package com.example.amoz.ui.components.text_fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    completionList: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    maxLines: Int = Int.MAX_VALUE
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(value) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        modifier = modifier,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { text ->
                selectedText = text
                onValueChange(text)
            },
            label = label,
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true),
            singleLine = singleLine,
            maxLines = maxLines,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            trailingIcon = {
                trailingIcon ?:  ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            colors = colors
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            completionList.filter { it.startsWith(selectedText, ignoreCase = true) }.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion) },
                    onClick = {
                        selectedText = suggestion
                        onValueChange(suggestion)
                        expanded = false
                    }
                )
            }
        }
    }
}