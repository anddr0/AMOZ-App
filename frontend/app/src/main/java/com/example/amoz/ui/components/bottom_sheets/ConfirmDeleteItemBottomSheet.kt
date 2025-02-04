package com.example.amoz.ui.components.bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteItemBottomSheet(
    onDismissRequest: () -> Unit,
    onDeleteConfirm: () -> Unit,
    itemNameToDelete: String,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    id = R.string.products_delete_product_template_confirm,
                    itemNameToDelete
                ),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            
            Text(text = stringResource(id = R.string.action_undone))

            Spacer(modifier = Modifier.height(20.dp))
            
            // -------------------- Cancel and apply --------------------
            PrimaryFilledButton(
                onClick = {
                    onDeleteConfirm()
                    onDismissRequest()
                },
                text = stringResource(id = R.string.confirm)
            )
            CloseOutlinedButton(
                onClick = onDismissRequest,
                text = stringResource(id = R.string.close)
            )
        }
    }
}