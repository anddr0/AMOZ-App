package com.example.amoz.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductDetails(
    @Serializable(with = UUIDSerializer::class)
    val productId: UUID,
    val name: String,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val category: CategoryDetails?,
    val mainProductVariant: ProductVariantDetails? = null,
    val productAttributes: List<ProductAttribute>,
    val description: String? = null,
    val brand: String? = null
)
