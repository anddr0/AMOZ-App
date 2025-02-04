package com.example.amoz.api.requests

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductVariantDetails
import com.example.validation.annotations.DecimalMin
import com.example.validation.annotations.Digits
import com.example.validation.annotations.NoDublicateAttributes
import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Positive
import com.example.validation.annotations.Size
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.*

@Serializable
data class ProductVariantCreateRequest(

    @Serializable(with = UUIDSerializer::class)
    @field:NotNullable(nameOfField = "Product variant of product")
    val productID: UUID? = null,

    @field:Size(max = 100, nameOfField = "Product variant name")
    @field:NotNullable(nameOfField = "Product variant name")
    val variantName: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, nameOfField = "Price")
    @field:Digits(integer = 10, fraction = 2, nameOfField = "Price")
    @field:NotNullable(nameOfField = "Price")
    @Serializable(with = BigDecimalSerializer::class)
    val variantPrice: BigDecimal? = null,

    @field:NotNullable(nameOfField = "Barcode")
    @field:Positive(nameOfField = "Barcode")
    val productVariantCode: Int? = null,

    @field:NotNullable(nameOfField = "Amount available in stock")
    val stock: StockCreateRequest? = null,

    val weight: WeightCreateRequest? = null,

    val dimensions: DimensionsCreateRequest? = null,

    @field:NoDublicateAttributes(nameOfField = "Product attributes")
    val variantAttributes: List<AttributeCreateRequest> = listOf()

) : ValidatableRequest<ProductVariantCreateRequest>() {
    constructor(productVariant: ProductVariantDetails?, productID: UUID?) : this(
        productID = productID,
        variantName = productVariant?.variantName,
        variantPrice = productVariant?.variantPrice,
        productVariantCode = productVariant?.code,
        stock = StockCreateRequest(productVariant?.stock),
        weight = productVariant?.weight?.let { WeightCreateRequest(it) },
        dimensions = productVariant?.dimensions?.let { DimensionsCreateRequest(it) },
        variantAttributes = productVariant?.variantAttributes?.map { AttributeCreateRequest(it) } ?: listOf(),
    )
}
