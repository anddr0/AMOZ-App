package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Employee
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.person.PersonImage
import com.example.amoz.ui.components.SwipeableItemWithActions
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@Composable
fun EmployeesLazyColumn(
    employees: List<Employee>,
    employeeImages: MutableMap<UUID, MutableStateFlow<ResultState<ImageBitmap?>>>,
    currentEmployee: Employee?,
    employeeProfileBottomSheetExpanded: Boolean,
    getProfilePicture: (UUID) -> Unit,
    expandEmployeeProfileBottomSheet: (Boolean) -> Unit,
    onAddEmployee: () -> Unit,
    onEmployeeDelete: (Employee) -> Unit,
) {
    val isCurrentEmployeeOwner = currentEmployee?.roleInCompany == RoleInCompany.OWNER

    var currentEmployeeToDisplay by remember {
        mutableStateOf<Employee?>(null)
    }

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        state = listState
    ) {
        items(employees, key = {it.employeeId}) { employee ->
            val isEmployeeVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == employees.indexOf(employee) }
                }
            }

            LaunchedEffect(isEmployeeVisible.value) {
                if (isEmployeeVisible.value) {
                    getProfilePicture(employee.employeeId)
                }
            }

            SwipeableItemWithActions(
                actions = {
                    EmployeeActionsRaw(
                        employee = employee,
                        isDeleteAvalible = currentEmployee?.employeeId != employee.employeeId,
                        onEmployeeProfileClick = {
                            currentEmployeeToDisplay = employee
                            expandEmployeeProfileBottomSheet(true)
                        },
                        onEmployeeDelete = if (isCurrentEmployeeOwner) {{
                            onEmployeeDelete(employee)
                        }} else null
                    )
                }
            ) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    leadingContent = { PersonImage(employeeImages[employee.employeeId]) },
                    headlineContent = { Text(text = employee.person.name + " " + employee.person.surname) },
                    supportingContent = { Text(text = employee.contactPerson.emailAddress ?: employee.contactPerson.contactNumber) },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            // -------------------- Add worker button --------------------
            if(isCurrentEmployeeOwner) {
                PrimaryFilledButton(
                    onClick = onAddEmployee,
                    text = stringResource(id = R.string.company_add_worker_button),
                    leadingIcon = Icons.Outlined.Add
                )
            }
        }
    }
    if (employeeProfileBottomSheetExpanded) {
        currentEmployeeToDisplay?.let{
            EmployeeProfileBottomSheet(
                employee = it,
                employeeProfileImage = employeeImages[it.employeeId],
                currentEmployeeRoleInCompany = it.roleInCompany,
                onDismissRequest = {
                    currentEmployeeToDisplay = null
                    expandEmployeeProfileBottomSheet(false)
                }
            )
        }
    }
}

