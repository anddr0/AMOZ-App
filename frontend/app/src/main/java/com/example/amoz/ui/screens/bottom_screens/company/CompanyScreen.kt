package com.example.amoz.ui.screens.bottom_screens.company

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.data.NavItem
import com.example.amoz.navigation.companyInfoScreenItemsMap
import com.example.amoz.ui.components.bottom_sheets.AddressBottomSheet
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.CompanyViewModel

@Composable
fun CompanyScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyViewModel = hiltViewModel(),
    companyInfoScreenItems: List<NavItem> = companyInfoScreenItemsMap.values.toList(),
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    LaunchedEffect (Unit) {
        companyViewModel.fetchCompanyDetailsOnScreenLoad()
    }

    val companyUIState by companyViewModel.companyUIState.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    val currentEmployeeRoleInCompany = companyUIState.currentEmployee?.roleInCompany ?: RoleInCompany.REGULAR
    val readOnly = currentEmployeeRoleInCompany == RoleInCompany.REGULAR

    val uploadCompanyImageSuccessText = stringResource(R.string.company_change_image_successful)
    val workersDescription = stringResource(R.string.company_employees_description)
    val customersDescription = stringResource(R.string.company_customers_description)

    val itemsDescriptions by remember {
        derivedStateOf {
            mutableStateListOf(
                workersDescription,
                customersDescription,
            )
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ResultStateView(
            state = companyUIState.company,
            onPullToRefresh = { companyViewModel.fetchCompanyDetails() }
        ) { company ->
            val address = company.address
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // --------------------- Company banner, logo ---------------------
                CompanyLogoAndName(
                    companyLogoState = companyUIState.companyLogo,
                    readOnly = readOnly,
                    companyName = company.name,
                    onCompanyNameClick = {
                        companyViewModel.expandChangeCompanyNameBottomSheet(true)
                    },
                    onCompanyImageClick = {
                        companyViewModel.expandChangeCompanyLogoBottomSheet(true)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --------------------- Company info ---------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    companyInfoScreenItems.zip(itemsDescriptions)
                        .forEach { (companyInfoItem, description) ->
                            CompanyInfoItem(
                                leadingIcon = companyInfoItem.icon,
                                title = stringResource(companyInfoItem.title),
                                itemDescription = description,
                                trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward,
                                onClick = {
                                    navController.navigate(companyInfoItem.screenRoute)
                                }
                            )
                        }

                    // ------------------- Company address -------------------
                    CompanyInfoItem(
                        leadingIcon = Icons.Outlined.LocationOn,
                        title = stringResource(R.string.company_address_screen),
                        itemDescription = address.fullAddress,
                        trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward,
                        onClick = {
                            companyViewModel.expandChangeCompanyAddressBottomSheet(true)
                        }
                    )

                    // ------------------- Company Number, Regon -------------------
                    CompanyInfoItem(
                        leadingIcon = null,
                        title = stringResource(R.string.company_number_name),
                        itemDescription = company.companyNumber,
                        trailingIcon = null,
                        onClick = {
                            clipboardManager.setText(
                                AnnotatedString(company.companyNumber)
                            )
                        }
                    )
                    company.regon?.let{
                        CompanyInfoItem(
                            leadingIcon = null,
                            title = stringResource(R.string.company_number_additional),
                            itemDescription = company.regon,
                            trailingIcon = null,
                            onClick = {
                                clipboardManager.setText(AnnotatedString(company.regon))
                            }
                        )
                    }
                }
            }

            if (companyUIState.changeCompanyAddressBottomSheetExpanded) {
                AddressBottomSheet(
                    readOnly = readOnly,
                    bottomSheetTitle = stringResource(id = R.string.address_change_title_company),
                    onDismissRequest = {
                        companyViewModel.expandChangeCompanyAddressBottomSheet(false)
                    },
                    address = companyUIState.companyCreateRequestState.address,
                    onDone = { request ->
                        companyViewModel.updateCompanyAddress(request, currentEmployeeRoleInCompany)
                    }
                )
            }

            if (companyUIState.changeCompanyNameBottomSheetExpanded) {
                ChangeCompanyNameBottomSheet(
                    company = companyUIState.companyCreateRequestState,
                    readOnly = readOnly,
                    onDismissRequest = {
                        companyViewModel.expandChangeCompanyNameBottomSheet(false)
                    },
                    onDone = { request ->
                        companyViewModel.updateCompany(request, currentEmployeeRoleInCompany)
                    }
                )
            }

            if (companyUIState.changeCompanyLogoBottomSheetExpanded) {
                ChangeCompanyLogoBottomSheet(
                    companyLogoState = companyUIState.companyLogo,
                    onImageChange = { imageUri ->
                        try {
                            companyViewModel.updateCompanyLogo(imageUri, currentEmployeeRoleInCompany)
                            callSnackBar(uploadCompanyImageSuccessText, Icons.Default.Done)
                        } catch (e: IllegalArgumentException) {
                            e.message?.let { callSnackBar(it, Icons.Default.Close) }
                        }
                    },
                    readOnly = readOnly,
                    onDismissRequest = {
                        companyViewModel.expandChangeCompanyLogoBottomSheet(false)
                    }
                )
            }


        }
    }
}

