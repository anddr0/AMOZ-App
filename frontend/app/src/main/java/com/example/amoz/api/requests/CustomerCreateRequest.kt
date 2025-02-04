package com.example.amoz.api.requests

import com.example.validation.annotations.NotNullable
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Customer
import kotlinx.serialization.Serializable

@Serializable
data class CustomerCreateRequest(
    @field:NotNullable(nameOfField = "Contact person")
    var contactPerson: ContactPersonCreateRequest = ContactPersonCreateRequest(),

    val defaultDeliveryAddress: AddressCreateRequest? = null
) : ValidatableRequest<CustomerCreateRequest>() {
    constructor(customer: Customer): this(
        contactPerson = ContactPersonCreateRequest(customer.contactPerson),
        defaultDeliveryAddress = customer.defaultDeliveryAddress?.let { AddressCreateRequest(it) }
    )
}
