package com.example.amoz.ui.screens.bottom_screens.company.customers

import com.example.amoz.data.B2BCustomer
import com.example.amoz.data.Person
import java.time.LocalDate

val testB2СCustomers = mutableListOf(
    Person(
        personId = 1,
        firstName = "John",
        lastName = "Johnson",
        sex = "Male",
        email = "johnjohnson@mail.com",
        phoneNumber = "+1-555-7549",
        dateOfBirth = LocalDate.of(2002, 4, 7)
    ),
    Person(
        personId = 2,
        firstName = "John",
        lastName = "Williams",
        sex = "Female",
        email = "johnwilliams@test.com",
        phoneNumber = "+1-555-9164",
        dateOfBirth = LocalDate.of(1993, 10, 5)
    ),
    Person(
        personId = 3,
        firstName = "Charlie",
        lastName = "Davis",
        sex = "Male",
        email = "charliedavis@test.com",
        phoneNumber = "+1-555-3033",
        dateOfBirth = LocalDate.of(1997, 4, 2)
    ),
    Person(
        personId = 4,
        firstName = "Emma",
        lastName = "Jones",
        sex = "Female",
        email = "emmajones@test.com",
        phoneNumber = "+1-555-2076",
        dateOfBirth = LocalDate.of(1999, 11, 5)
    ),
    Person(
        personId = 5,
        firstName = "David",
        lastName = "Doe",
        sex = "Male",
        email = "daviddoe@mail.com",
        phoneNumber = "+1-555-1204",
        dateOfBirth = LocalDate.of(1999, 4, 26)
    ),
    Person(
        personId = 6,
        firstName = "Grace",
        lastName = "Davis",
        sex = "Female",
        email = "gracedavis@test.com",
        phoneNumber = "+1-555-1785",
        dateOfBirth = LocalDate.of(1988, 8, 16)
    ),
    Person(
        personId = 7,
        firstName = "David",
        lastName = "Miller",
        sex = "Male",
        email = "davidmiller@example.com",
        phoneNumber = "+1-555-4941",
        dateOfBirth = LocalDate.of(1995, 11, 28)
    ),
    Person(
        personId = 8,
        firstName = "David",
        lastName = "Davis",
        sex = "Female",
        email = "daviddavis@mail.com",
        phoneNumber = "+1-555-2622",
        dateOfBirth = LocalDate.of(1996, 10, 24)
    ),
    Person(
        personId = 9,
        firstName = "Grace",
        lastName = "Williams",
        sex = "Female",
        email = "gracewilliams@mail.com",
        phoneNumber = "+1-555-1001",
        dateOfBirth = LocalDate.of(2001, 9, 30)
    ),
    Person(
        personId = 10,
        firstName = "Jane",
        lastName = "Doe",
        sex = "Female",
        email = "janedoe@test.com",
        phoneNumber = "+1-555-3250",
        dateOfBirth = LocalDate.of(2009, 1, 7)
    ),
    Person(
        personId = 11,
        firstName = "John",
        lastName = "Brown",
        sex = "Female",
        email = "johnbrown@test.com",
        phoneNumber = "+1-555-7122",
        dateOfBirth = LocalDate.of(1993, 4, 11)
    ),
    Person(
        personId = 12,
        firstName = "Bob",
        lastName = "Johnson",
        sex = "Male",
        email = "bobjohnson@example.com",
        phoneNumber = "+1-555-9767",
        dateOfBirth = LocalDate.of(1998, 1, 25)
    ),
    Person(
        personId = 13,
        firstName = "John",
        lastName = "Johnson",
        sex = "Female",
        email = "johnjohnson@test.com",
        phoneNumber = "+1-555-8601",
        dateOfBirth = LocalDate.of(1988, 4, 20)
    ),
    Person(
        personId = 14,
        firstName = "Bob",
        lastName = "Davis",
        sex = "Male",
        email = "bobdavis@test.com",
        phoneNumber = "+1-555-3589",
        dateOfBirth = LocalDate.of(1992, 3, 20)
    ),
    Person(
        personId = 15,
        firstName = "Bob",
        lastName = "Johnson",
        sex = "Female",
        email = "bobjohnson@test.com",
        phoneNumber = "+1-555-1123",
        dateOfBirth = LocalDate.of(2010, 9, 14)
    ),
    Person(
        personId = 16,
        firstName = "Bob",
        lastName = "Davis",
        sex = "Male",
        email = "bobdavis@test.com",
        phoneNumber = "+1-555-5477",
        dateOfBirth = LocalDate.of(2000, 8, 26)
    ),
    Person(
        personId = 17,
        firstName = "Bob",
        lastName = "Williams",
        sex = "Male",
        email = "bobwilliams@example.com",
        phoneNumber = "+1-555-1287",
        dateOfBirth = LocalDate.of(1995, 7, 13)
    ),
    Person(
        personId = 18,
        firstName = "John",
        lastName = "Jones",
        sex = "Female",
        email = "johnjones@test.com",
        phoneNumber = "+1-555-1843",
        dateOfBirth = LocalDate.of(1984, 9, 16)
    ),
    Person(
        personId = 19,
        firstName = "Jane",
        lastName = "Johnson",
        sex = "Female",
        email = "janejohnson@example.com",
        phoneNumber = "+1-555-2155",
        dateOfBirth = LocalDate.of(2001, 9, 22)
    ),
    Person(
        personId = 20,
        firstName = "David",
        lastName = "Williams",
        sex = "Male",
        email = "davidwilliams@example.com",
        phoneNumber = "+1-555-8622",
        dateOfBirth = LocalDate.of(1999, 5, 20)
    )
)


val testB2BCustomers = mutableListOf(
    B2BCustomer(
        companyName = "Tech Corp",
        email = "contact@techcorp.com",
        companyAddress = "123 Silicon Avenue",
        companyIdentifier = "TC123",
    ),
    B2BCustomer(
        companyName = "Green Solutions",
        email = "info@greensolutions.com",
        companyAddress = "456 Eco Road",
        companyIdentifier = "GS456",
    ),
    B2BCustomer(
        companyName = "Health Plus",
        email = "support@healthplus.com",
        companyAddress = "789 Wellness Street",
        companyIdentifier = "HP789",
    ),
    B2BCustomer(
        companyName = "Tech Corp",
        email = "contact@techcorp.com",
        companyAddress = "123 Silicon Avenue",
        companyIdentifier = "TC123",
    ),
    B2BCustomer(
        companyName = "Green Solutions",
        email = "info@greensolutions.com",
        companyAddress = "456 Eco Road",
        companyIdentifier = "GS456",
    ),
    B2BCustomer(
        companyName = "Health Plus",
        email = "support@healthplus.com",
        companyAddress = "789 Wellness Street",
        companyIdentifier = "HP789",
    ),
    B2BCustomer(
        companyName = "Tech Corp",
        email = "contact@techcorp.com",
        companyAddress = "123 Silicon Avenue",
        companyIdentifier = "TC123",
    ),
    B2BCustomer(
        companyName = "Green Solutions",
        email = "info@greensolutions.com",
        companyAddress = "456 Eco Road",
        companyIdentifier = "GS456",
    ),
    B2BCustomer(
        companyName = "Health Plus",
        email = "support@healthplus.com",
        companyAddress = "789 Wellness Street",
        companyIdentifier = "HP789",
    ),
    B2BCustomer(
        companyName = "Tech Corp",
        email = "contact@techcorp.com",
        companyAddress = "123 Silicon Avenue",
        companyIdentifier = "TC123",
    ),
    B2BCustomer(
        companyName = "Green Solutions",
        email = "info@greensolutions.com",
        companyAddress = "456 Eco Road",
        companyIdentifier = "GS456",
    ),
    B2BCustomer(
        companyName = "Health Plus",
        email = "support@healthplus.com",
        companyAddress = "789 Wellness Street",
        companyIdentifier = "HP789",
    )
)