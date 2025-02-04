package com.example.amoz.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.PrimaryOutlinedButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.dropdown_menus.SexDropdownMenu
import com.example.amoz.ui.components.text_fields.DateTextField
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.EmployeeViewModel

import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditingScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    employeeViewModel: EmployeeViewModel,
)
{
    val employeeUiState by employeeViewModel.employeeUiState.collectAsState()
    var validateMessage by remember { mutableStateOf<String?>(null) }

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ResultStateView(employeeUiState.fetchedEmployeeState) { employee ->
            var employeeBody by remember { mutableStateOf(employee) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),

            ) {
                ImageWithIcon(
                    image = employeeViewModel.userImageBitmap ?: employeeViewModel.selectedNewImageUri?.toString(),
                    onImagePicked = {
                        employeeViewModel.selectedNewImageUri = it
                        employeeViewModel.userImageBitmap = null
                                    },
                    size = 160.dp,
                    isEditing = true,
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                Text(
                    text = "About you",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Column (
                    Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),

                    ){
                    OutlinedTextField(
                        value = employeeBody.person.name,
                        onValueChange = {  if (it.length <= 30) {
                            employeeBody = employeeBody.copy(person = employeeBody.person.copy(name = it))
                        }},
                        label = { Text(stringResource(R.string.profile_first_name)) },
                        modifier = Modifier
                            .height(R.dimen.text_field_height.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = employeeBody.person.surname,
                        onValueChange = { if (it.length <= 30) {
                            employeeBody = employeeBody.copy(person = employeeBody.person.copy(surname = it))
                        }},
                        label = { Text(stringResource(R.string.profile_last_name)) },
                        modifier = Modifier
                            .height(R.dimen.text_field_height.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = employeeBody.contactPerson.emailAddress?: "",
                        onValueChange = { employeeBody = employeeBody.copy(contactPerson = employeeBody.contactPerson.copy(emailAddress = it)) },
                        label = { Text(stringResource(R.string.email_optional)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = employeeBody.contactPerson.contactNumber,
                        onValueChange = { employeeBody = employeeBody.copy(contactPerson = employeeBody.contactPerson.copy(contactNumber = it)) },
                        label = { Text(stringResource(R.string.profile_phone_number)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        singleLine = true,

                        )

                    SexDropdownMenu(
                        selectedSex = employeeBody.person.sex,
                        onSexChange = { employeeBody = employeeBody.copy(person = employeeBody.person.copy(sex = it)) }
                    )

                    DateTextField(
                        label = stringResource(R.string.profile_birth_date),
                        date = employeeBody.person.dateOfBirth,
                        onDateChange = {
                            employeeBody = employeeBody.copy(person = employeeBody.person.copy(dateOfBirth = it as LocalDate))
                        },
                        trailingIcon = Icons.Default.DateRange,
                        showTime = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 32.dp)
                )

                validateMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                PrimaryFilledButton(
                    onClick = {
                        val userRegisterRequest = UserRegisterRequest(employeeBody)
                        validateMessage = userRegisterRequest.validate()

                        if (validateMessage == null) {
                            employeeViewModel.updateUser(userRegisterRequest, navController)
                        }
                              },
                    text = stringResource(R.string.save),
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryOutlinedButton(
                    onClick = { navController.popBackStack() },
                    text = stringResource(R.string.cancel),
                )
            }
        }
    }
}