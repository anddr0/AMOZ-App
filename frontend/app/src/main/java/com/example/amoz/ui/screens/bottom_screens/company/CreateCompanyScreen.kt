package com.example.amoz.ui.screens.bottom_screens.company

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.ui.components.ErrorText
import com.example.amoz.ui.components.ImageWithText
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.PrimaryOutlinedButton
import com.example.amoz.ui.components.bottom_sheets.AddressBottomSheet
import com.example.amoz.ui.components.text_fields.AddressTextField
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.CompanyViewModel

@Composable
fun CreateCompanyScreen (
    navController: NavHostController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyViewModel,
){
    val companyUIState by companyViewModel.companyUIState.collectAsState()

    var companyCreateRequestState by remember { mutableStateOf(companyViewModel.companyCreateRequestState.value) }
    var validationErrorMessage by remember { mutableStateOf<String?>(null) }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.company_create_text),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            ImageWithText(
                image = pickedImageUri.toString(),
                size = 300.dp,
                shape = RoundedCornerShape(40.dp),
                onImagePicked = { uri ->
                    uri?.let {
                        companyViewModel.updateNewCompanyImageUri(uri)                        }
                },
                onRemoveImage = {
                    pickedImageUri = null
                }
            )

            OutlinedTextField(
                value = companyCreateRequestState.name,
                onValueChange = { companyCreateRequestState = companyCreateRequestState.copy(name = it) },
                label = { Text(stringResource(R.string.company_name)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = companyCreateRequestState.companyNumber,
                onValueChange = { companyCreateRequestState = companyCreateRequestState.copy(companyNumber = it) },
                label = { Text(stringResource(R.string.company_number)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = companyCreateRequestState.regon ?: "",
                onValueChange = { companyCreateRequestState = companyCreateRequestState.copy(regon = it) },
                label = { Text(stringResource(R.string.company_number_additional_optional)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            AddressTextField (
                address = companyCreateRequestState.address,
                onClick = {
                    companyViewModel.expandChangeCompanyAddressBottomSheet(true)
                },
                trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward
            )

            ErrorText(errorMessage = validationErrorMessage)

            PrimaryFilledButton(
                text = stringResource(id = R.string.company_register),
                onClick = {
                    companyViewModel.updateCompanyCreateRequest(companyCreateRequestState)
                    try {
                        companyViewModel.createCompany(navController)
                    } catch (e: Exception) {
                        validationErrorMessage = e.message
                    }
                }
            )

            if (companyUIState.changeCompanyAddressBottomSheetExpanded) {
                AddressBottomSheet(
                    bottomSheetTitle = stringResource(id = R.string.address_change_title_company),
                    onDismissRequest = {
                        companyViewModel.expandChangeCompanyAddressBottomSheet(false)
                    },
                    address = companyViewModel.companyCreateRequestState.collectAsState().value.address,
                    onDone = { request ->
                        companyCreateRequestState = companyCreateRequestState.copy(
                            address = request,
                            countryOfRegistration = request.country)
                    }
                )

            }
        }
    }
}