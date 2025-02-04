package com.example.amoz.api.requests

import com.example.validation.annotations.Min
import com.example.validation.annotations.NotNullable
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderItemDetails
import com.example.amoz.models.ProductOrderItemSummary
import com.example.validation.annotations.NotBlank
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.*

@Serializable
data class ProductOrderItemCreateRequest(

    @field:NotNullable(nameOfField = "Product variant ID")
    @Serializable(with = UUIDSerializer::class)
    val productVariantId: UUID,

    @field:Min(value = 1, nameOfField = "Amount")
    @field:NotNullable(nameOfField = "Amount")
    val amount: Int
) : ValidatableRequest<ProductOrderItemCreateRequest>() {
    constructor(productOrderItemSummary: ProductOrderItemSummary) : this(
        productVariantId = productOrderItemSummary.productVariant.productVariantId,
        amount = productOrderItemSummary.amount
    )

    constructor(productOrderItemDetails: ProductOrderItemDetails) : this(
        productVariantId = productOrderItemDetails.productVariant.productVariantId,
        amount = productOrderItemDetails.amount
    )
}

