package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.models.User
import com.example.amoz.api.repositories.UserRepository
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import com.example.amoz.app.SignOutManager
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.states.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val userRepository: UserRepository,
    private val signOutManager: SignOutManager,
) : BaseViewModel() {

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState

    // ----------------------------------------------------

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
        _userUiState.update { UserUiState() }
    }

    // ----------------------------------------------------

    fun registerUser(navController: NavHostController) {
       val validationMessage = _userUiState.value.currentUserRegisterRequest?.validate()
        if (validationMessage == null){
            performRepositoryAction(
                null,
                "Could not sign up. Try again later.",
                action = {
                    _userUiState.value.currentUserRegisterRequest?.let {
                        userRepository.registerUser(it)
                    }
                }, onSuccess = {
                    updatePushToken()
                    uploadProfilePicture()
                    navController.navigate(Screens.NoCompany.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        } else {
            Log.e("UserViewModel", "Validation error: $validationMessage")
            throw IllegalArgumentException(validationMessage)
        }
    }

    private fun updatePushToken() {
        performRepositoryAction(null,
            action =  {
                userRepository.updatePushToken()
            })
    }

    private fun uploadProfilePicture() {
        performRepositoryAction(
            null,
            "Could not upload profile picture. Try again later.",
            action = {
                _userUiState.value.currentImageUri?.let {
                    userRepository.uploadProfilePicture(file = it.toMultipartBodyPart(context))
                }
            }
        )
    }

    fun isUserRegisteredRedirect(navController: NavHostController, onSuccess: (Boolean) -> Unit) {
        performRepositoryAction(
            binding = _userUiState.value.isRegisteredState,
            action = {
                userRepository.isUserRegistered()
            }, onSuccess = {
                updatePushToken()
                if (!it) {
                  navController.navigate(Screens.Register.route){
                      popUpTo(0){inclusive = true}
                  }
                } else {
                    onSuccess(true)
                }
            }
        )
    }


    fun navigateUser(navController: NavHostController) {
        viewModelScope.launch {
            isUserRegisteredRedirect(navController) {}

            val state = _userUiState.value.isRegisteredState.first {
                it !is ResultState.Loading
            }

            when (state) {
                is ResultState.Success -> {
                    if (state.data) {
                        navController.navigate(Screens.NoCompany.route){
                            popUpTo(0){inclusive = true}
                        }
                    } else {
                        navController.navigate(Screens.Register.route) {}
                    }
                }
                is ResultState.Failure -> {
                    Log.e("UserViewModel", "Error: ${state.message}")
                }
                else -> {
                    Log.w("UserViewModel", "Unexpected state: $state")
                }
            }
        }
    }

    fun updateCurrentUserRegisterRequest(personCreateRequest: PersonCreateRequest, contactPersonCreateRequest: ContactPersonCreateRequest) {
        val user = UserRegisterRequest(
            person = personCreateRequest,
            contactPerson = contactPersonCreateRequest
        )

        val validationErrorMessage = user.validate()
        if (validationErrorMessage == null) {
            _userUiState.update {
                it.copy(currentUserRegisterRequest = user)
            }
        } else {
            Log.e("UserViewModel", "Validation error: $validationErrorMessage")
            throw IllegalArgumentException(validationErrorMessage)
        }
    }

    fun updateCurrentUserImageUri(uri: Uri) {
        _userUiState.update {
            it.copy(currentImageUri = uri)
        }
    }
}
