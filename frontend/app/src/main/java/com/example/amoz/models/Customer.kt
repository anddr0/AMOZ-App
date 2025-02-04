package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Customer(
    @Serializable(with = UUIDSerializer::class)
    val customerId: UUID,
    val contactPerson: ContactPerson,
    val defaultDeliveryAddress: Address? = null
)
