package com.example.amoz.ui.states

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Employee
import kotlinx.coroutines.flow.MutableStateFlow

data class EmployeeUiState (
    val isProfileDropdownExpanded: Boolean = false,
    val isProfileDatePickerVisible: Boolean = false,

    val fetchedEmployeeState: MutableStateFlow<ResultState<Employee>> = MutableStateFlow(ResultState.Idle),
    val fetchedEmployeeImageState: MutableStateFlow<ResultState<ImageBitmap>> = MutableStateFlow(ResultState.Idle),
    val currentUserRegisterRequest: UserRegisterRequest? = null,
)