package com.example.amoz.ui.screens.bottom_screens.additional_screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.enums.AppThemeMode
import com.example.amoz.ui.components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeAppThemeBottomSheet(
    currentAppThemeMode: AppThemeMode,
    onComplete: (AppThemeMode) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var appThemeModeState by remember { mutableStateOf(currentAppThemeMode) }
    var expanded by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -------------------- Bottom sheet title --------------------
            Text(
                text = stringResource(R.string.app_theme_mode_edit),
                style = MaterialTheme.typography.headlineSmall
            )

            // -------------------- Theme Mode Selection --------------------
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = appThemeModeState.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.select_app_theme)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    AppThemeMode.values().forEach { mode ->
                        DropdownMenuItem(
                            text = { Text(mode.name) },
                            onClick = {
                                appThemeModeState = mode
                                expanded = false
                            }
                        )
                    }
                }
            }

            // -------------------- Done Button --------------------
            PrimaryFilledButton(
                text = stringResource(R.string.save),
                onClick = {
                    onComplete(appThemeModeState)
                    onDismissRequest()
                },
                leadingIcon = Icons.Default.Done
            )
        }
    }
}