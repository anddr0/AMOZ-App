package com.example.amoz.ui.screens.bottom_screens.company.employees

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.Employee
import com.example.amoz.ui.components.person.PersonProfileColumn
import com.example.amoz.ui.components.PrimaryFilledButton
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeProfileBottomSheet(
    onDismissRequest: () -> Unit,
    employee: Employee,
    currentEmployeeRoleInCompany: RoleInCompany,
    employeeProfileImage: MutableStateFlow<ResultState<ImageBitmap?>>?,
) {
    val isCurrentEmployeeOwner = currentEmployeeRoleInCompany == RoleInCompany.OWNER

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {

        val listItemColors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val listItemModifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    brush = SolidColor(MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(5.dp)
                )

            // -------------------- Person profile data --------------------
            PersonProfileColumn(
                personProfileImage = employeeProfileImage,
                currentEmployeeRoleInCompany = currentEmployeeRoleInCompany,
                personFirstName = employee.person.name,
                personLastName = employee.person.surname,
                personEmail = employee.contactPerson.emailAddress ?: "No email address",
                personPhoneNumber = employee.contactPerson.contactNumber,
                personSex = employee.person.sex,
                personBirthDate = employee.person.dateOfBirth
            )

            HorizontalDivider()

            // -------------------- Employment date --------------------
            ListItem(
                modifier = listItemModifier,
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.CalendarToday, contentDescription = null)
                },
                overlineContent = {
                    Text(text = stringResource(id = R.string.profile_employment_date))
                },
                headlineContent = { Text(text = employee.employmentDate.format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                )) },
                trailingContent = {
                    if(isCurrentEmployeeOwner)
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                },
                colors = listItemColors
            )

            // -------------------- Role in company --------------------
            ListItem(
                modifier = listItemModifier,
                leadingContent = {
                    Icon(imageVector = Icons.Outlined.Business, contentDescription = null)
                },
                overlineContent = {Text(text = stringResource(id = R.string.company_employee_role))},
                headlineContent = { Text(text =
                    employee.roleInCompany.toString().toLowerCase().capitalize()
                ) },
                colors = listItemColors
            )


        }
    }
}

