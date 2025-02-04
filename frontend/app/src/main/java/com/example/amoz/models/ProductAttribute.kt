package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class ProductAttribute(
    @Serializable(with = UUIDSerializer::class)
    val productAttributeId: UUID,
    val attribute: Attribute,
    val value: String? = null
)
