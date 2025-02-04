package com.example.amoz.ui.states

import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.R
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.requests.CustomerB2BCreateRequest
import com.example.amoz.api.requests.CustomerB2CCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Company
import com.example.amoz.models.CustomerB2B
import com.example.amoz.models.CustomerB2C
import com.example.amoz.models.Employee
import com.example.amoz.models.Invitation
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

data class CompanyUIState (
    val companyLogo: MutableStateFlow<ResultState<ImageBitmap>> = MutableStateFlow(ResultState.Idle),

    val company: MutableStateFlow<ResultState<Company>> = MutableStateFlow(ResultState.Idle),
    val companyCreateRequestState: CompanyCreateRequest = CompanyCreateRequest(),
    val employees: MutableStateFlow<ResultState<List<Employee>>> = MutableStateFlow(ResultState.Idle),
    val currentEmployee: Employee? = null,
    val employeeToDelete: Employee? = null,
    val employeeImages: MutableStateFlow<MutableMap<UUID, MutableStateFlow<ResultState<ImageBitmap?>>>> = MutableStateFlow(
        HashMap()
    ),

    val fetchedInvitationListState: MutableStateFlow<ResultState<List<Invitation>>> = MutableStateFlow(
        ResultState.Idle
    ),

    val companyB2BCustomers: MutableStateFlow<ResultState<List<CustomerB2B>>> = MutableStateFlow(
        ResultState.Idle
    ),
    val currentCustomerB2B: CustomerB2B? = null,
    val customerB2BCreateRequestState: CustomerB2BCreateRequest = CustomerB2BCreateRequest(),

    val companyB2CCustomers: MutableStateFlow<ResultState<List<CustomerB2C>>> = MutableStateFlow(
        ResultState.Idle
    ),
    val currentCustomerB2C: CustomerB2C? = null,
    val customerB2CCreateRequestState: CustomerB2CCreateRequest = CustomerB2CCreateRequest(),

    val changeCompanyNameBottomSheetExpanded: Boolean = false,

    val deleteEmployeeBottomSheetExpanded: Boolean = false,
    val changeCompanyAddressBottomSheetExpanded: Boolean = false,
    val changeCompanyLogoBottomSheetExpanded: Boolean = false,
    val addEmployeeBottomSheetExpanded: Boolean = false,
    val employeeProfileBottomSheetExpanded: Boolean = false,
    val addB2CCustomerBottomSheetExpanded: Boolean = false,
    val addB2BCustomerBottomSheetExpanded: Boolean = false,
)