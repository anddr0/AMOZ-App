package com.example.amoz.api.requests

import com.example.amoz.api.enums.UnitWeight
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Stock
import com.example.amoz.models.Weight
import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Positive
import kotlinx.serialization.Serializable

@Serializable
data class WeightCreateRequest(
    @field:NotNullable(nameOfField = "Unit weight")
    val unitWeight: UnitWeight? = null,

    @field:Positive(nameOfField = "Amount")
    @field:NotNullable(nameOfField = "Amount")
    val amount: Double? = null
) : ValidatableRequest<WeightCreateRequest>() {
    constructor(weight: Weight) : this(
        unitWeight = weight.unitWeight,
        amount = weight.amount
    )
}
