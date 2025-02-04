package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ContactPerson
import com.example.amoz.models.Employee
import com.example.amoz.models.Person
import com.example.validation.annotations.NotNullable
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(
    @field:NotNullable(nameOfField = "Person")
    val person: PersonCreateRequest,

    @field:NotNullable(nameOfField = "Contact person")
    val contactPerson: ContactPersonCreateRequest
) : ValidatableRequest<UserRegisterRequest>() {
    constructor(person: Person, contactPerson: ContactPerson): this(
        person = PersonCreateRequest(person),
        contactPerson = ContactPersonCreateRequest(contactPerson)
    )

    constructor(employee: Employee): this(
        person = employee.person,
        contactPerson = employee.contactPerson
    )
}
