package com.example.amoz.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.amoz.R

@Composable
fun CustomDialogWindow(
    title: String,
    text: String,
    confirmationText: String = stringResource(id = R.string.yes),
    declineText: String = stringResource(id = R.string.no),
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        confirmButton = {
            TextButton(onClick = { onAccept() }) {
                Text(stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = { onReject() }) {
                Text(stringResource(id = R.string.no))
            }
        }
    )
}
