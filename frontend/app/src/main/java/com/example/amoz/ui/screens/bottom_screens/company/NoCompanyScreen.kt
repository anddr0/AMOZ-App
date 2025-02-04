package com.example.amoz.ui.screens.bottom_screens.company

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.models.Invitation
import com.example.amoz.ui.components.CompanyInvitationListItem
import com.example.amoz.ui.components.CustomDialogWindow
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.PrimaryOutlinedButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.AuthenticationViewModel
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.view_models.EmployeeViewModel

@Composable
fun NoCompanyScreen (
    navController: NavHostController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyViewModel,
    employeeViewModel: EmployeeViewModel = hiltViewModel(),
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    val companyUIState by companyViewModel.companyUIState.collectAsState()
    var showAcceptDialogWindow by remember { mutableStateOf(false) }
    var selectedInvitation by remember { mutableStateOf<Invitation?>(null) }

    LaunchedEffect(Unit) {
        Log.d("NoCompanyScreen", "LaunchedEffect")
        companyViewModel.isUserInCompany{ isInCompany ->
            if (!isInCompany) {
                Log.d("NoCompanyScreen", "Is in company: $isInCompany")
                companyViewModel.fetchInvitations()
            } else {
                Log.d("NoCompanyScreen", "Is in company: $isInCompany")
                navController.navigate(Screens.Company.route){
                    popUpTo(0) { inclusive = true}
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ResultStateView(
            companyUIState.fetchedInvitationListState,
            onPullToRefresh = { companyViewModel.fetchInvitations()
            }) { invitationsList ->
            var mutableInvitationsList by remember { mutableStateOf(invitationsList) }

            if (invitationsList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(id = R.string.company_no_company),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryFilledButton(
                        text = stringResource(id = R.string.company_register),
                        onClick = {
                            navController.navigate(Screens.CreateCompany.route)
                        }
                    )
                    PrimaryOutlinedButton(
                        onClick = {
                            authenticationViewModel.signOut(context as Activity) {
                                navController.navigate(Screens.Entry.route){
                                    popUpTo(0)
                                }
                            }
                        },
                        text = stringResource(R.string.profile_sign_out),
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(id = R.string.your_company_invites),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    items(
                        mutableInvitationsList,
                        key = { it.token }
                    ) { invitation ->
                        CompanyInvitationListItem (
                            invitation = invitation,
                            onAcceptClick = {
                                selectedInvitation = invitation
                                showAcceptDialogWindow = true
                            },
                            onDeclineClick = {
                                employeeViewModel.declineInvitation(invitation.token.toString())
                                mutableInvitationsList = invitationsList.filter { it.token != invitation.token }
                            }
                        )
                    }

                    item {
                        PrimaryFilledButton(
                            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                            text = stringResource(id = R.string.or_company_register),
                            onClick = {
                                navController.navigate(Screens.CreateCompany.route)
                            }
                        )
                        PrimaryOutlinedButton(
                            onClick = {
                                authenticationViewModel.signOut(context as Activity) {
                                    navController.navigate(Screens.Entry.route){
                                        popUpTo(0)
                                    }
                                }
                            },
                            text = stringResource(R.string.profile_sign_out),
                        )
                    }
                }
            }
        }
    }

    if (showAcceptDialogWindow) {
        CustomDialogWindow(
            title = stringResource(id = R.string.accept_invitation_window_title),
            text = stringResource(id = R.string.accept_invitation_window_text),
            onDismiss = { showAcceptDialogWindow = false },
            onAccept = {
                employeeViewModel.acceptInvitation(selectedInvitation?.token.toString()) {
                    companyViewModel.fetchCompanyDetails()
                    companyViewModel.fetchEmployees()
                    navController.navigate(Screens.Company.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                showAcceptDialogWindow = false
                selectedInvitation = null
            },
            onReject = {
                showAcceptDialogWindow = false
                selectedInvitation = null
            }
        )
    }
}