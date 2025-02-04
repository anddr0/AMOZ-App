package com.example.amoz.ui.screens.profile

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.PrimaryOutlinedButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.AuthenticationViewModel
import com.example.amoz.view_models.EmployeeViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    employeeViewModel: EmployeeViewModel,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
)
{
    val employeeUiState by employeeViewModel.employeeUiState.collectAsState()
    val context = LocalContext.current
    val activity = remember { context as? Activity }

    LaunchedEffect(true) {
        employeeViewModel.fetchEmployeeOnScreenLoad()
    }

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ResultStateView(employeeUiState.fetchedEmployeeState) { employee ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            ) {
                ImageWithIcon(
                    image = employeeViewModel.userImageBitmap,
                    contentDescription = "Profile Picture",
                    size = 160.dp,
                    shape = CircleShape,
                    isEditing = false
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

                    )
                {
                    Text(
                        text = "${stringResource(R.string.profile_first_name)}: ${employee.person.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "${stringResource(R.string.profile_last_name)}: ${employee.person.surname}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "${stringResource(R.string.profile_email)}: ${employee.contactPerson.emailAddress ?: ""}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "${stringResource(R.string.profile_phone_number)}: ${employee.contactPerson.contactNumber}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "${stringResource(R.string.profile_sex)}: ${employee.person.sex.getName()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "${stringResource(R.string.profile_birth_date)}: ${employee.person.dateOfBirth}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 32.dp)
                )

                PrimaryFilledButton(
                    onClick = { navController.navigate(Screens.ProfileEdit.route) },
                    text = stringResource(R.string.profile_edit_profile),
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryOutlinedButton(
                    onClick = {
                        activity?.let { activity ->
                            authenticationViewModel.signOut(activity) {
                                navController.navigate(Screens.Entry.route) {
                                    popUpTo(0)
                                }
                            }
                        }
                    },
                    text = stringResource(R.string.profile_sign_out),
                )
            }
        }
    }
}