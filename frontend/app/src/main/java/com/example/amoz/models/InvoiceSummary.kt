package com.example.amoz.models

import com.example.amoz.api.serializers.BigDecimalSerializer
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Serializable
data class InvoiceSummary(
    @Serializable(with = UUIDSerializer::class)
    val invoiceId: UUID,
    val invoiceNumber: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val amountOnInvoice: BigDecimal,
    val orderItems: List<ProductOrderItemSummary>,
    @Serializable(with = LocalDateSerializer::class)
    val issueDate: LocalDate
)
