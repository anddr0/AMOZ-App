package com.example.amoz.models

import com.example.amoz.api.enums.UnitWeight
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Weight(
    @Serializable(with = UUIDSerializer::class)
    val weightId: UUID,
    val unitWeight: UnitWeight,
    val amount: Double
)
