package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.amoz.api.repositories.EmployeeRepository
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.SignOutManager
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.ui.states.EmployeeUiState
import com.example.amoz.ui.states.OrderUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmployeeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository,
    private val signOutManager: SignOutManager,
) : BaseViewModel() {

    private val _employeeUiState = MutableStateFlow(EmployeeUiState())
    val employeeUiState: StateFlow<EmployeeUiState> = _employeeUiState

    var selectedNewImageUri: Uri? by mutableStateOf(null)
    var userImageBitmap: ImageBitmap? by mutableStateOf(null)

    init {
        observeSignOutEvent()
    }

    private fun observeSignOutEvent() {
        viewModelScope.launch {
            signOutManager.signOutEvent.collect {
                clearState()
            }
        }
    }

    private fun clearState() {
        _employeeUiState.update { EmployeeUiState() }
        selectedNewImageUri = null
        userImageBitmap = null
    }

    private fun fetchEmployee() {
        performRepositoryAction(
            employeeUiState.value.fetchedEmployeeState,
            "Could not fetch employee.",
            action = {
                employeeRepository.fetchEmployee()
                     },
        )
    }

    private fun fetchEmployeeImage() {
        performRepositoryAction(
            employeeUiState.value.fetchedEmployeeImageState,
            "Could not fetch employee image.",
            action = {
                val imageBitmap = userRepository.getProfilePicture()
                userImageBitmap = imageBitmap
                imageBitmap
            }
        )
    }

    fun fetchEmployeeOnScreenLoad() {
        if (employeeUiState.value.fetchedEmployeeState.value is ResultState.Idle) {
            fetchEmployee()
            fetchEmployeeImage()
        }
    }

     fun updateUser(userRegisterRequest: UserRegisterRequest, navController: NavController) {
        performRepositoryAction(
            null,
            "Could not update employee.",
            action = { userRepository.updateUser(userRegisterRequest) },
            onSuccess = {
                updateUserImage(navController)
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateUserImage(navController: NavController) {
        if (selectedNewImageUri != null) {
            performRepositoryAction(
                null,
                "Could not update user image.",
                action = { userRepository.uploadProfilePicture(selectedNewImageUri!!.toMultipartBodyPart(context)) },
                onSuccess = {
                    fetchEmployee()
                    fetchEmployeeImage()
                    navController.popBackStack()
                },
            )
        } else {
            fetchEmployee()
            navController.popBackStack()
        }
    }

    fun manageInvitation(token: String, isAccepted: Boolean) {
        if (isAccepted) {
            acceptInvitation(token){}
        } else {
            declineInvitation(token)
        }
    }

    fun acceptInvitation(token: String, onSuccess: () -> Unit) {
        performRepositoryAction(
            null,
            "Could not accept invitation.",
            action = { employeeRepository.acceptInvitationToCompany(token) },
            onSuccess = {
                fetchEmployee()
                fetchEmployeeImage()
            }
        )
    }

    fun declineInvitation(token: String) {
        performRepositoryAction(
            null,
            "Could not decline invitation.",
            action = { employeeRepository.rejectInvitationToCompany(token) }
        )
    }

    // ----------------------------------------------------

    fun changeProfileDropDownExpanded(isExpanded: Boolean) {
        _employeeUiState.value = _employeeUiState.value.copy(isProfileDropdownExpanded = isExpanded)
    }


}
