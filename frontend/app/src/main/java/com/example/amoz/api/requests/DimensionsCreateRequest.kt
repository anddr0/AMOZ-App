package com.example.amoz.api.requests

import com.example.validation.annotations.NotNullable
import com.example.validation.annotations.Positive
import com.example.amoz.api.enums.UnitDimensions
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CategoryDetails
import com.example.amoz.models.Dimensions
import kotlinx.serialization.Serializable

@Serializable
data class DimensionsCreateRequest(
    @field:NotNullable(nameOfField = "Unit dimensions")
    val unitDimensions: UnitDimensions? = null,

    @field:Positive(nameOfField = "Height")
    val height: Double? = null,

    @field:Positive(nameOfField = "Lenght")
    val length: Double? = null,

    @field:Positive(nameOfField = "Width")
    val width: Double? = null
) : ValidatableRequest<DimensionsCreateRequest>() {
    constructor(dimensions: Dimensions) : this(
        unitDimensions = dimensions.unitDimensions,
        height = dimensions.height,
        length = dimensions.length,
        width = dimensions.width
    )
}
