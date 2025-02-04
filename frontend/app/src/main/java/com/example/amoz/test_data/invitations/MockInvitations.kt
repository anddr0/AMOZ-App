package com.example.amoz.test_data.invitations

import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.api.enums.Sex
import com.example.amoz.api.enums.SystemRole
import com.example.amoz.models.Address
import com.example.amoz.models.Company
import com.example.amoz.models.ContactPerson
import com.example.amoz.models.Employee
import com.example.amoz.models.Invitation
import com.example.amoz.models.Person
import com.example.amoz.models.User
import java.time.LocalDate
import java.util.UUID

fun createMockInvitations(): List<Invitation> {
    val mockCompany = Company(
        companyId = UUID.randomUUID(),
        companyNumber = "123456789",
        name = "Tech Solutions",
        countryOfRegistration = "Poland",
        address = Address(
            addressId = UUID.randomUUID(),
            country = "Poland",
            city = "Warsaw",
            street = "Main Street",
            streetNumber = "42",
            apartmentNumber = "10A",
            postalCode = "00-001",
            additionalInformation = "Near Central Station"
        ),
        regon = "987654321"
    )

    val mockPerson = Person(
        personId = UUID.randomUUID(),
        name = "John",
        surname = "Doe",
        dateOfBirth = LocalDate.of(1990, 1, 1),
        sex = Sex.M
    )

    val mockContactPerson = ContactPerson(
        contactPersonId = UUID.randomUUID(),
        contactNumber = "+48 123 456 789",
        emailAddress = "john.doe@example.com"
    )

    val mockUser = User(
        userId = UUID.randomUUID().toString(),
        systemRole = SystemRole.USER
    )

    val mockEmployee = Employee(
        employeeId = UUID.randomUUID(),
        user = mockUser,
        contactPerson = mockContactPerson,
        person = mockPerson,
        roleInCompany = RoleInCompany.REGULAR,
        employmentDate = LocalDate.of(2020, 5, 15)
    )

    return List(5) { // Creating 5 mock invitations
        Invitation(
            company = mockCompany,
            sender = mockEmployee,
            token = UUID.randomUUID()
        )
    }
}