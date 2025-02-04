package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class CategorySummary(
    @Serializable(with = UUIDSerializer::class)
    val categoryId: UUID,
    val name: String,
    val categoryLevel: Short
) {
    constructor(categoryDetails: CategoryDetails) :this(
        categoryId = categoryDetails.categoryId,
        name = categoryDetails.name,
        categoryLevel = categoryDetails.categoryLevel
    )
}
