package com.example.amoz.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.requests.CustomerB2CCreateRequest
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.dropdown_menus.SexDropdownMenu
import com.example.amoz.ui.components.pickers.AddressPicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditB2CCustomerBottomSheet(
    onDismissRequest: () -> Unit,
    customerRequest: CustomerB2CCreateRequest,
    onDone: (CustomerB2CCreateRequest) -> Unit
) {
    var customerState by remember { mutableStateOf(customerRequest) }
    val person = customerState.person
    val contactPerson = customerState.customer.contactPerson

    var validationMessage by remember { mutableStateOf<String?>(null) }

    var isDatePickerVisible by remember { mutableStateOf(false)}
    val dateState = rememberDatePickerState()

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
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // -------------------- Title --------------------
            Text(
                text = stringResource(id = R.string.company_customers_add_customer),
                style = MaterialTheme.typography.headlineMedium
            )

            // -------------------- Customer's first name --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.profile_first_name))
                },
                value = customerState.person.name,
                onValueChange = {
                    customerState = customerState.copy(person = customerState.person.copy(name = it))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.TextFields, contentDescription = null)
                },
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Customer's last name --------------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.profile_last_name))
                },
                value = person.surname,
                onValueChange = {
                    customerState = customerState.copy(person = customerState.person.copy(surname = it))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.TextFields, contentDescription = null)
                },
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Customer's sex --------------------
            SexDropdownMenu(
                selectedSex = customerState.person.sex,
                onSexChange = { newSex ->
                    customerState = customerState.copy(person = customerState.person.copy(sex = newSex))
                },
            )

            // -------------------- Customer's email --------------------
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

            // -------------------- Birth date --------------------
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        brush = SolidColor(MaterialTheme.colorScheme.outline),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable {
                        isDatePickerVisible = true
                    },
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(id = R.string.profile_birth_date)) },
                headlineContent = {
                    Text(
                        text = customerState.person.dateOfBirth.format(
                            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        )
                    )
                },
                trailingContent = { Icon(imageVector = Icons.Outlined.Edit, null) },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            )

            // -------------------- Address --------------------
            AddressPicker(
                address = customerState.customer.defaultDeliveryAddress ?: AddressCreateRequest(),
                onAddressChange = { newAddress ->
                    customerState = customerState.copy(
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

    if (isDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateState.selectedDateMillis?.let {
                            val dateOfBirth = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                            customerState = customerState.copy(person = customerState.person
                                .copy(dateOfBirth = dateOfBirth))
                        }
                        isDatePickerVisible = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}