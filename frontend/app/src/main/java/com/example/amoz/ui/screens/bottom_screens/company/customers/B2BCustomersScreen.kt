package com.example.amoz.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.models.CustomerB2B
import com.example.amoz.pickers.CustomerPicker
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.ui.theme.AmozApplicationTheme

@Composable
fun B2BCustomerScreen(
    navController: NavHostController,
    b2bCustomersList: List<CustomerB2B>,
    companyViewModel: CompanyViewModel,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    val companyUiState by companyViewModel.companyUIState.collectAsState()
    val customerPicker = CustomerPicker(navController)

    val customerAddSuccessful = stringResource(id = R.string.company_add_customer_successful)
    val customerEditSuccessful = stringResource(id = R.string.company_update_customer_successful)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        // -------------------- B2C Customers --------------------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(b2bCustomersList) { customerB2B ->
                ListItem(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            if (customerPicker.isCustomerPickerMode()) {
                                customerPicker.pickCustomer(customerB2B)
                            }
                            else {
                                companyViewModel.updateCustomerB2BCreateRequestState(customerB2B)
                                companyViewModel.expandAddEditB2BCustomerBottomSheet(true)
                            }
                        },
                    headlineContent = { Text(customerB2B.nameOnInvoice) },
                    supportingContent = { Text(text = customerB2B.address.fullAddress) },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }
        }
        // -------------------- FAB --------------------
        if(!customerPicker.isCustomerPickerMode()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        companyViewModel.updateCustomerB2BCreateRequestState(null)
                        companyViewModel.expandAddEditB2BCustomerBottomSheet(true)
                    },
                    modifier = Modifier
                        .padding(16.dp),
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text(text = "Add customer") }
                )
            }
        }
    }
    if (companyUiState.addB2BCustomerBottomSheetExpanded) {
        AddEditB2BCustomerBottomSheet(
            customer = companyUiState.customerB2BCreateRequestState,
            onDone = { request ->
                companyUiState.currentCustomerB2B?.let {
                    companyViewModel.updateB2BCustomer(it.customer.customerId, request) {
                        callSnackBar(customerEditSuccessful, Icons.Outlined.Done)
                    }
                } ?: companyViewModel.createB2BCustomer(request) {
                    callSnackBar(customerAddSuccessful, Icons.Outlined.Done)
                }
            },
            onDismissRequest = {
                companyViewModel.updateCustomerB2BCreateRequestState(null)
                companyViewModel.expandAddEditB2BCustomerBottomSheet(false)
            },
        )
    }
}

