package com.example.amoz.ui.screens.bottom_screens.company.customers

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.api.requests.CustomerB2BCreateRequest
import com.example.amoz.ui.components.pickers.AddressPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditB2BCustomerBottomSheet(
    onDismissRequest: () -> Unit,
    customer: CustomerB2BCreateRequest,
    onDone: (CustomerB2BCreateRequest) -> Unit
) {
    var customerState by remember {  mutableStateOf(customer) }
    val contactPerson = customerState.customer.contactPerson
    var validationMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(customerState) { validationMessage = null }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // -------------------- Title --------------------
            Text(
                text = stringResource(id = R.string.company_customers_add_customer),
                style = MaterialTheme.typography.headlineMedium
            )

            // -------------------- Company name --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.company_name))
                },
                value = customerState.nameOnInvoice,
                onValueChange = {
                    customerState = customerState.copy(nameOnInvoice = it)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DriveFileRenameOutline,
                        contentDescription = null
                    )
                },
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Customer's phone number --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.profile_phone_number))
                },
                value = contactPerson.contactNumber,
                onValueChange = {
                    customerState = customerState
                        .copy(customer = customerState.customer
                            .copy(contactPerson = customerState.customer.contactPerson
                                .copy(contactNumber = it)))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Company email --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.profile_email))
                },
                value = contactPerson.emailAddress ?: "",
                onValueChange = {
                    customerState = customerState
                        .copy(customer = customerState.customer
                            .copy(contactPerson = customerState.customer.contactPerson
                                .copy(emailAddress = it)))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Company number --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.company_number))
                },
                value = customerState.companyNumber,
                onValueChange = {
                    customerState = customerState.copy(companyNumber = it)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Numbers, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Company Address --------------------
            AddressPicker(
                address = customerState.address,
                onAddressChange = { newAddress ->
                    customerState = customerState.copy(
                        address = newAddress,
                        customer = customerState.customer.copy(defaultDeliveryAddress = newAddress)
                    )
                }
            )

            // -------------------- Confirm button --------------------
            Spacer(modifier = Modifier.height(15.dp))

            validationMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            PrimaryFilledButton(
                onClick = {
                    val violation = customerState.validate()
                    if (violation != null) {
                        Log.d("CUSTOMER STATE", customerState.toString())
                        validationMessage = violation
                    }
                    else {
                        onDone(customerState)
                        onDismissRequest()
                    }
                },
                enabled = true,
                text = stringResource(id = R.string.save)
            )
        }
    }
}