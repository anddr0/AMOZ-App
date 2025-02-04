package com.example.amoz.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductVariantDetails(
    @Serializable(with = UUIDSerializer::class)
    val productVariantId: UUID,
    val code: Int,
    val stock: Stock,
    val dimensions: Dimensions? = null,
    val weight: Weight? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal,
    val variantName: String? = null,
    val variantAttributes: List<VariantAttribute> = listOf()
)
