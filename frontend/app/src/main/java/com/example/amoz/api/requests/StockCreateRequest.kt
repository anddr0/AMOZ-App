package com.example.amoz.api.requests

import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Dimensions
import com.example.amoz.models.Stock
import com.example.validation.annotations.Min
import com.example.validation.annotations.NotNullable

import kotlinx.serialization.Serializable

@Serializable
data class StockCreateRequest(

    @field:Min(value = 0, nameOfField = "Amount available")
    @field:NotNullable(nameOfField = "Amount available")
    val amountAvailable: Int? = null,

    @field:Min(value = 0, nameOfField = "Alarming amount")
    val alarmingAmount: Int? = null
) : ValidatableRequest<StockCreateRequest>() {
    constructor(stock: Stock?) : this(
        amountAvailable = stock?.amountAvailable,
        alarmingAmount = stock?.alarmingAmount
    )
}
