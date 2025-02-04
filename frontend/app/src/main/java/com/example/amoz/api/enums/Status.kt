package com.example.amoz.api.enums

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    NEW, ORDERED, SHIPPED, DELIVERED, CANCELLED;

    val color: Color
        get() = when (this) {
            NEW -> Color.Gray
            ORDERED -> Color.Blue
            SHIPPED -> Color.Cyan
            DELIVERED -> Color.Green
            CANCELLED -> Color.Red
        }

    fun getName(): String {
        return when (this) {
            NEW -> "New"
            ORDERED -> "Ordered"
            SHIPPED -> "Shipped"
            DELIVERED -> "Delivered"
            CANCELLED -> "Cancelled"
        }
    }
}