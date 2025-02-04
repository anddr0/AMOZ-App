package com.example.amoz.ui.screens.bottom_screens.additional_screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.amoz.ui.components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeCurrencyBottomSheet(
    currentCurrency: String,
    onComplete: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var currencyState by remember { mutableStateOf(currentCurrency) }

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
                text = stringResource(R.string.currency_edit),
                style = MaterialTheme.typography.headlineSmall
            )

            // -------------------- Currency --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = currencyState,
                onValueChange = {
                    if (it.length < 5) {
                        currencyState = it
                    }
                },
                placeholder = { Text(stringResource(R.string.default_currency)) },
                label = { Text(stringResource(R.string.currency)) }
            )
            PrimaryFilledButton(
                text = stringResource(R.string.save),
                onClick = {
                    onComplete(currencyState)
                    onDismissRequest()
                },
                leadingIcon = Icons.Default.Done
            )
        }
    }
}