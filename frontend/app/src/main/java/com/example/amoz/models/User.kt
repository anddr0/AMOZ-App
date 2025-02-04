package com.example.amoz.models

import com.example.amoz.api.enums.SystemRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual

@Serializable
data class User(
    val userId: String,
    val systemRole: SystemRole
)
