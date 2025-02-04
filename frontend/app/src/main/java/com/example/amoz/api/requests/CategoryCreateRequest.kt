package com.example.amoz.api.requests

import com.example.amoz.api.requests.field_names.CategoryFieldNames
import com.example.validation.annotations.NotBlank
import com.example.amoz.api.serializers.UUIDSerializer
import com.example.amoz.interfaces.ValidatableRequest
import com.example.amoz.models.CategoryDetails
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CategoryCreateRequest(

    @field:NotBlank(nameOfField = CategoryFieldNames.CATEGORY_NAME)
    val name: String = "",

    @Serializable(with = UUIDSerializer::class)
    val parentCategoryId: UUID? = null
) : ValidatableRequest<CategoryCreateRequest>() {
    constructor(category: CategoryDetails) : this(
        name = category.name,
        parentCategoryId = category.parentCategory?.categoryId
    )
}
