package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class Stock(
    @Serializable(with = UUIDSerializer::class)
    val stockId: UUID,
    val amountAvailable: Int,
    val alarmingAmount: Int? = null,
    val isAlarmTriggered: Boolean
)
