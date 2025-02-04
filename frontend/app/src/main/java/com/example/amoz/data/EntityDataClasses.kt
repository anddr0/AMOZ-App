package com.example.amoz.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

// --------------------- Person, employees, customers ---------------------
data class Person(
    val personId: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val sex: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val personPhoto: Int? = null,
    val dateOfBirth: LocalDate = LocalDate.now(),
)

data class Employee(
    val employeeId: Int? = null,
    val firstName: String = "",
    val lastName: String = "",
    val sex: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val personPhoto: Int? = null,
    val dateOfBirth: LocalDate = LocalDate.now(),
    val employmentDate: LocalDate = LocalDate.now(),
)

data class B2BCustomer(
    val companyName: String = "",
    val email: String = "",
    val companyAddress: String = "",
    val companyIdentifier: String = "",
    val phoneNumber: String? = null,
)
// --------------------- Category ---------------------
@Serializable
data class Category(
    @Contextual val categoryId: UUID,
    val name: String,
    val categoryLevel: Short,
    val childCategories: List<Category> = listOf()
)

// --------------------- Address ---------------------
data class Address (
    val street: String = "",
    val houseNumber: String = "",
    val apartmentNumber: String = "",
    val city: String = "",
    val postalCode: String = "",
    val additionalInfo: String = "",
)

// --------------------- Navigation Items ---------------------
data class NavItem(
    val title: Int,
    val screenRoute: String,
    val icon: ImageVector,
    val unselectedIcon: ImageVector? = null,
)

data class ProductVariant(
    val id: String = UUID.randomUUID().toString(),
    val productId: String = "",
    val barcode: Int = 1234567890,
    var name: String = "",
    var impactOnPrice: Double = 0.0,
    var image: Bitmap? = null,
    var attributes: Map<String, String> = emptyMap(),
)
