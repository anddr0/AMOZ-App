package com.example.amoz.models

import kotlinx.serialization.Serializable

@Serializable
data class CustomerB2C(
    val customer: Customer,
    val person: Person
)
