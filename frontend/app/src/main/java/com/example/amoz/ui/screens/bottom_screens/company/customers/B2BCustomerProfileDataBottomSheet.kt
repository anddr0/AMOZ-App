package com.example.amoz.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amoz.R
import com.example.amoz.models.CustomerB2B
import com.example.amoz.view_models.CompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun B2BCustomerProfileDataBottomSheet(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(5.dp))
        .border(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp)
        ),
    companyViewModel: CompanyViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    b2BCustomer: CustomerB2B
) {
    val listItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
    val clipboardManager = LocalClipboardManager.current

    val contactPerson = b2BCustomer.customer.contactPerson

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // -------------------- Company name --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.nameOnInvoice)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Business, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(R.string.company_name)) },
                headlineContent = { Text(text = b2BCustomer.nameOnInvoice) },
                colors = listItemColors
            )

            // -------------------- Email --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        contactPerson.emailAddress?.let {
                            clipboardManager.setText(
                                AnnotatedString(it)
                            )
                        }
                    }
                ),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Mail, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(id = R.string.profile_email)) },
                headlineContent = { Text(text = contactPerson.emailAddress ?: "No email") },
                colors = listItemColors
            )

            // -------------------- Company address --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.address.fullAddress)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Place, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(R.string.company_address)) },
                headlineContent = { Text(text = b2BCustomer.address.fullAddress) },
                colors = listItemColors
            )

            // -------------------- Company address --------------------
            ListItem(
                modifier = modifier.then(Modifier
                    .clickable {
                        clipboardManager.setText(
                            AnnotatedString(b2BCustomer.companyNumber)
                        )
                    }),
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Numbers, contentDescription = null)
                },
                overlineContent = { Text(text = stringResource(R.string.company_number)) },
                headlineContent = { Text(text = b2BCustomer.companyNumber) },
                colors = listItemColors
            )
        }
    }
}