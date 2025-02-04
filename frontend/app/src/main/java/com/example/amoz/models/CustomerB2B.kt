package com.example.amoz.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class CustomerB2B(
    val customer: Customer,
    val address: Address,
    val nameOnInvoice: String,
    val companyNumber: String
)
