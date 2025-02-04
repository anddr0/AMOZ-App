package com.example.amoz.ui.components.bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.MarkunreadMailbox
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.ui.components.PrimaryFilledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBottomSheet(
    bottomSheetTitle: String = stringResource(id = R.string.address_change_title),
    address: AddressCreateRequest = AddressCreateRequest(),
    readOnly: Boolean = false,
    onDismissRequest: () -> Unit,
    onDone: (AddressCreateRequest) -> Unit
) {
    var addressState by remember { mutableStateOf(address) }
    var validationMessage by remember { mutableStateOf<String?>(null) }

    val regularValueLength = 255
    val shortValueLength = 10
    val postalCodeValueLength = 5

    val focusRequesters = remember { List(7) { FocusRequester() } }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = bottomSheetTitle,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            // -------------------- Street --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[0]),
                label = { Text(text = stringResource(id = R.string.street)) },
                value = addressState.street,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        addressState = addressState.copy(street = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Place, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[1].requestFocus() }
                ),
//                isError = !isStreetStateValid,
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )

            // -------------------- House number --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[1]),
                label = { Text(text = stringResource(id = R.string.house_number)) },
                value = addressState.streetNumber,
                onValueChange = {
                    if (it.length <= shortValueLength) {
                        addressState = addressState.copy(streetNumber = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[2].requestFocus() }
                ),
//                isError = !isHouseNumberStateValid,
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )

            // -------------------- Apartment number --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[2]),
                label = { Text(text = stringResource(id = R.string.apartment_number)) },
                value = addressState.apartmentNumber ?: "",
                onValueChange = {
                    if (it.length <= shortValueLength) {
                        addressState = addressState.copy(apartmentNumber = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.DoorFront, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[3].requestFocus() }
                ),
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )

            // -------------------- City/town --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[3]),
                label = { Text(text = stringResource(id = R.string.city)) },
                value = addressState.city,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        addressState = addressState.copy(city = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.LocationCity, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[4].requestFocus() }
                ),
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )

            // -------------------- Postal code --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[4]),
                label = { Text(text = stringResource(id = R.string.postal_code)) },
                value = addressState.postalCode,
                onValueChange = {
                    if (it.length <= postalCodeValueLength && it.isDigitsOnly()) {
                        addressState = addressState.copy(postalCode = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.MarkunreadMailbox, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[5].requestFocus() }
                ),
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )
            // -------------------- Country --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[5]),
                label = { Text(text = stringResource(id = R.string.country)) },
                value = addressState.country,
                onValueChange = {
                    if (it.length <= regularValueLength) {
                        addressState = addressState.copy(country = it)
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.AccountBalance, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesters[6].requestFocus() }
                ),
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )
            // -------------------- Additional info --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[6]),
                label = { Text(text = stringResource(id = R.string.additional_info)) },
                value = addressState.additionalInformation ?: "",
                onValueChange = {
                    addressState = addressState.copy(additionalInformation = it)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                singleLine = true,
                readOnly = readOnly
            )

            // -------------------- Validation --------------------
            validationMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // -------------------- Done button --------------------
            if (!readOnly) {
                Spacer(modifier = Modifier.height(5.dp))
                PrimaryFilledButton(
                    onClick = {
                        val violation = addressState.validate()
                        if (violation != null) {
                            validationMessage = violation
                        } else {
                            onDone(addressState)
                            onDismissRequest()
                        }
                    },
                    text = stringResource(id = R.string.save),
                    leadingIcon = Icons.Outlined.Done
                )
            }
        }
    }
}