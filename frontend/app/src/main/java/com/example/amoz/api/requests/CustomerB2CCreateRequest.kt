package com.example.amoz.api.requests

import com.example.validation.annotations.NotNullable
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CustomerB2C
import kotlinx.serialization.Serializable

@Serializable
data class CustomerB2CCreateRequest(
    @field:NotNullable(nameOfField = "Customer")
    val customer: CustomerCreateRequest = CustomerCreateRequest(),

    @field:NotNullable(nameOfField = "Person")
    var person: PersonCreateRequest = PersonCreateRequest()
) : ValidatableRequest<CustomerB2CCreateRequest>() {
    constructor(customerB2C: CustomerB2C): this(
        customer = CustomerCreateRequest(customerB2C.customer),
        person = PersonCreateRequest(customerB2C.person)
    )
}
