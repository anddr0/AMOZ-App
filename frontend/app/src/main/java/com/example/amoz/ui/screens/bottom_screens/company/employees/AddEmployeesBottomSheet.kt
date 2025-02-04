package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeesBottomSheet(
    onInvitationSend: (String) -> Unit,
    onDismissRequest: () -> Unit,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    var workerEmail by remember { mutableStateOf("") }

    val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    var isEmailValid by remember { mutableStateOf(false) }

    fun validateEmail(email: String) {
        isEmailValid = email.matches(emailPattern.toRegex())
    }

    val invSentSuccessful = stringResource(id = R.string.company_add_worker_invite_successful)

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // -------------------- Title --------------------
            Text(
                text = stringResource(id = R.string.company_add_worker_invite_title),
                style = MaterialTheme.typography.headlineMedium
            )

            // -------------------- Worker's email --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.company_add_worker_email_textfield))
                },
                value = workerEmail,
                onValueChange = {
                    workerEmail = it
                    validateEmail(workerEmail)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Confirm button --------------------
            PrimaryFilledButton(
                onClick = {
                    onInvitationSend(workerEmail)
                    onDismissRequest()
                    callSnackBar(
                        invSentSuccessful,
                        Icons.Outlined.Done
                    )
                },
                enabled = isEmailValid,
                text = stringResource(id = R.string.company_add_worker_email_send_inv)
            )
        }
    }
}