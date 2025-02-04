package com.example.amoz.ui.screens.bottom_screens.company.employees

import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.amoz.models.Employee

@Composable
fun EmployeeActionsRaw(
    employee: Employee,
    isDeleteAvalible: Boolean,
    onEmployeeProfileClick: () -> Unit,
    onEmployeeDelete: (() -> Unit)? = null,
) {
    val context = LocalContext.current

    // -------------------- Delete worker from database --------------------
    if (onEmployeeDelete != null && isDeleteAvalible) {
        IconButton(onClick = onEmployeeDelete) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Kick employee from company")
        }
    }

    // -------------------- Employee profile --------------------
    IconButton(onClick = onEmployeeProfileClick) {
        Icon(imageVector = Icons.Outlined.Person, contentDescription = "Profile")
    }

    // -------------------- Worker's email --------------------
    IconButton(
        onClick = {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${employee.contactPerson.emailAddress}")
            }
            context.startActivity(emailIntent)
        }
    ) {
        Icon(imageVector = Icons.Outlined.Mail, contentDescription = "Email")
    }

    // -------------------- Worker's phone --------------------
    employee.contactPerson.contactNumber.let { phoneNumber ->
        IconButton(
            onClick = {
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                context.startActivity(callIntent)
            }
        ) {
            Icon(imageVector = Icons.Outlined.Phone, contentDescription = "Call")
        }
    }
}