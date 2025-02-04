package com.example.amoz.api.requests

import com.example.validation.annotations.ListSize
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.PastOrPresent
import com.example.validation.annotations.Size
import com.example.amoz.api.enums.Status
import com.example.amoz.api.serializers.LocalDateTimeSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductOrderDetails
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.time.LocalDateTime
import java.util.*

@Serializable
data class ProductOrderCreateRequest(
    @field:NotNullable(nameOfField = "Status")
    val status: Status,

    @field:ListSize(min = 1, nameOfField = "Product order items")
    val productOrderItems: List<ProductOrderItemCreateRequest>,

    val address: AddressCreateRequest? = null,

    @Serializable(with = UUIDSerializer::class)
    val customerId: UUID? = null,

    @field:Size(max = 10, nameOfField = "Tracking number")
    val trackingNumber: String? = null,

    @field:PastOrPresent(nameOfField = "Time of sending")
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeOfSending: LocalDateTime? = null
) : ValidatableRequest<ProductOrderCreateRequest>() {
    constructor(productOrder: ProductOrderDetails) : this(
        status = productOrder.status,
        productOrderItems = productOrder.productOrderItems.map { ProductOrderItemCreateRequest(it) },
        address = if (productOrder.address != null) {
            AddressCreateRequest(productOrder.address)
        } else {
            null
        },
        customerId = productOrder.customer?.customerId,
        trackingNumber = productOrder.trackingNumber,
        timeOfSending = productOrder.timeOfSending
    )

    constructor() : this (
        status = Status.NEW,
        productOrderItems = listOf(),
    )
}
