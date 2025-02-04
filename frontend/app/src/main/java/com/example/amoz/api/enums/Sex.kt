package com.example.amoz.api.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Sex {
    M, F, O;

    fun getName(): String {
        return when (this) {
            M -> "Male"
            F -> "Female"
            O -> "Other"
        }
    }
}

