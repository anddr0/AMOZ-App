package com.example.amoz.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.UUID

@Serializable
data class ProductSummary(
    @Serializable(with = UUIDSerializer::class)
    val productId: UUID,
    val name: String,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val category: CategorySummary?,
    val mainProductVariant: ProductVariantDetails? = null,
    val description: String? = null,
    val brand: String? = null
) {
    constructor(productDetails: ProductDetails) :this (
        productId = productDetails.productId,
        name = productDetails.name,
        price = productDetails.price,
        category = productDetails.category?.let {CategorySummary(it)},
        mainProductVariant = productDetails.mainProductVariant,
        description = productDetails.description,
        brand = productDetails.brand
    )
}
