package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CustomerAnyRepresentation(
    @Serializable(with = UUIDSerializer::class)
    val customerId: UUID,
    val nameOnInvoice: String,
    val phoneNumber: String,
    val emailAddress: String?
) {
    constructor(b2bCustomer: CustomerB2B) : this (
        customerId = b2bCustomer.customer.customerId,
        nameOnInvoice = b2bCustomer.nameOnInvoice,
        phoneNumber = b2bCustomer.customer.contactPerson.contactNumber,
        emailAddress = b2bCustomer.customer.contactPerson.emailAddress
    )
    constructor(b2cCustomer: CustomerB2C) : this (
        customerId = b2cCustomer.customer.customerId,
        nameOnInvoice = "${b2cCustomer.person.name} ${b2cCustomer.person.surname}",
        phoneNumber = b2cCustomer.customer.contactPerson.contactNumber,
        emailAddress = b2cCustomer.customer.contactPerson.emailAddress
    )
}