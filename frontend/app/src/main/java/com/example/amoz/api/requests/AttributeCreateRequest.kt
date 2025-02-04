package com.example.amoz.api.requests

import com.example.validation.annotations.NotBlank
import com.example.validation.annotations.Size
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.Attribute

import com.example.amoz.models.CategoryDetails
import com.example.amoz.models.ProductAttribute
import com.example.amoz.models.VariantAttribute
import kotlinx.serialization.Serializable


@Serializable
data class AttributeCreateRequest(

    @field:NotBlank(nameOfField = "Attribute name")
    @field:Size(max = 50, nameOfField = "Attribute name")
    val attributeName: String,

    @field:Size(max = 255, nameOfField = "Attribute value")
    val value: String? = null
) : ValidatableRequest<AttributeCreateRequest>() {
    constructor(productAttribute: ProductAttribute) : this(
        attributeName = productAttribute.attribute.attributeName,
        value = productAttribute.value
    )
    constructor(variantAttribute: VariantAttribute) : this(
        attributeName = variantAttribute.attribute.attributeName,
        value = variantAttribute.value
    )
}
