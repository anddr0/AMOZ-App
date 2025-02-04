package com.example.amoz.api.requests

import com.example.validation.annotations.DecimalMin
import com.example.validation.annotations.Digits
import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Size
import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Dimensions
import com.example.amoz.models.ProductDetails
import com.example.validation.annotations.NoDublicateAttributes
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.math.BigDecimal
import java.util.*

@Serializable
data class ProductCreateRequest(

    @field:NotBlank(nameOfField = "Name")
    @field:Size(max = 100, nameOfField = "Name")
    val name: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, nameOfField = "Price")
    @field:Digits(integer = 10, fraction = 2, nameOfField = "Price")
    @field:NotNullable(nameOfField = "Price")
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal? = null,

    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID? = null,

    @field:Size(max = 1000, nameOfField = "Description")
    val description: String? = null,

    @field:Size(max = 50, nameOfField = "Brand")
    val brand: String? = null,

    val productVariantIds: List<@Serializable(with = UUIDSerializer::class) UUID> = listOf(),

    @field:NoDublicateAttributes(nameOfField = "Product attributes")
    val productAttributes: List<AttributeCreateRequest> = listOf()
) : ValidatableRequest<ProductCreateRequest>() {
    constructor(productDetails: ProductDetails?, productVariantIds: List<UUID> = emptyList()) : this(
        name = productDetails?.name,
        price = productDetails?.price,
        categoryId = productDetails?.category?.categoryId,
        description = productDetails?.description,
        brand = productDetails?.brand,
        productVariantIds = productVariantIds,
        productAttributes = productDetails?.productAttributes?.map { AttributeCreateRequest(it) } ?: emptyList()
    )
}
