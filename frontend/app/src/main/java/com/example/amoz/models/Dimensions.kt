package com.example.amoz.models

import com.example.amoz.api.enums.UnitDimensions
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Dimensions(
    @Serializable(with = UUIDSerializer::class)
    val dimensionsId: UUID,
    val unitDimensions: UnitDimensions,
    val height: Double,
    val length: Double,
    val width: Double
)
