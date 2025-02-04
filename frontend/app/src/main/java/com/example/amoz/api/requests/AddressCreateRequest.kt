package com.example.amoz.api.requests


import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.Size
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Address
import kotlinx.serialization.Serializable

@Serializable
data class AddressCreateRequest(
    @field:NotBlank(nameOfField = "Country")
    @field:Size(max = 50)
    var country: String = "",
    @field:NotBlank(nameOfField = "City")
    @field:Size(max = 50)
    var city: String = "",

    @field:NotBlank(nameOfField = "Street")
    @field:Size(max = 50)
    var street: String = "",

    @field:NotBlank(nameOfField = "Street number")
    @field:Size(max = 10)
    var streetNumber: String = "",

    @field:Size(max = 10, nameOfField = "Apartment number")
    var apartmentNumber: String? = null,

    @field:NotBlank(nameOfField = "Postal code")
    @field:Size(max = 10, nameOfField = "Postal code")
    var postalCode: String = "",

    @field:Size(max = 255, nameOfField = "Additional information")
    var additionalInformation: String? = null
) : ValidatableRequest<AddressCreateRequest>() {
    val fullAddress: String
        get() = listOfNotNull(street, streetNumber, apartmentNumber, city, postalCode)
            .filter { it.isNotBlank() }
            .joinToString(", ")

    constructor(address: Address) : this(
        country = address.country,
        city = address.city,
        street = address.street,
        streetNumber = address.streetNumber,
        apartmentNumber = address.apartmentNumber,
        postalCode = address.postalCode,
        additionalInformation = address.additionalInformation
    )
}
