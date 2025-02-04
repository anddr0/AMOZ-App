package com.example.amoz.ui.states

import android.net.Uri
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Employee
import kotlinx.coroutines.flow.MutableStateFlow

data class UserUiState (

    val fetchedUserInfo: ResultState<Employee> = ResultState.Idle,

    val isRegisteredState: MutableStateFlow<ResultState<Boolean>> = MutableStateFlow(ResultState.Idle),
    val currentUserRegisterRequest:UserRegisterRequest?  = null,
    val currentImageUri: Uri? = null
)