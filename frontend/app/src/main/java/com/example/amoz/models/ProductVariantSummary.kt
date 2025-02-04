package com.example.amoz.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductVariantSummary(
    @Serializable(with = UUIDSerializer::class)
    val productVariantId: UUID,
    val code: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal,
    val variantName: String? = null
)
