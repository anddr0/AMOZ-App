package com.example.amoz.api.requests

import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Past
import com.example.validation.annotations.Size
import com.example.amoz.api.enums.Sex
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Person
import kotlinx.serialization.Serializable

import kotlinx.serialization.Contextual
import java.time.LocalDate

@Serializable
data class PersonCreateRequest(

    @field:NotBlank(nameOfField = "Name")
    @field:Size(min = 2, max = 30, nameOfField = "Name")
    val name: String = "",

    @field:NotBlank(nameOfField = "Surname")
    @field:Size(min = 2, max = 30, nameOfField = "Surname")
    val surname: String = "",

    @field:Past(nameOfField = "Date of birth")
    @Serializable(with = LocalDateSerializer::class)
    val dateOfBirth: LocalDate = LocalDate.of(2000, 1, 1),

    @field:NotNullable(nameOfField = "Sex")
    val sex: Sex = Sex.M
) : ValidatableRequest<PersonCreateRequest>() {
    constructor(person: Person): this(
        name = person.name,
        surname = person.surname,
        dateOfBirth = person.dateOfBirth,
        sex = person.sex
    )
}
