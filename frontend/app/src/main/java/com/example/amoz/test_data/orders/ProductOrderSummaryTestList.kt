import com.example.amoz.models.*
import com.example.amoz.api.enums.Status
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

fun mockProductOrderSummaries(): List<ProductOrderSummary> {
    return listOf(
        ProductOrderSummary(
            productOrderId = UUID.fromString("2a93d8f9-8467-4d43-b8fc-16b650d9a684"),
            status = Status.ORDERED,
            sampleProductOrderItem = ProductOrderItemSummary(
                productOrderItemId = UUID.fromString("a2e9f5c4-4d52-40b3-bf3b-ecbb94c79e7b"),
                productVariant = ProductVariantSummary(
                    productVariantId = UUID.fromString("e7e7d0ff-64a4-45f1-929b-e7e0d6e8e4b5"),
                    code = 12345,
                    variantPrice = BigDecimal("199.99"),
                    variantName = "Czarny T-shirt, rozmiar M"
                ),
                unitPrice = BigDecimal("299.99"),
                amount = 2,
                productName = "\"Koszulka, Czerwona\""
            ),
            totalDue = BigDecimal("199.98"),
            trackingNumber = "\"ABC123456789\"",
            timeOfSending = LocalDateTime.parse("2024-12-02T10:01:49.046"),
            timeOfCreation = LocalDateTime.parse("2024-12-02T10:01:49.046")
        ),
        // Add more mock items if needed
        ProductOrderSummary(
            productOrderId = UUID.randomUUID(),
            status = Status.SHIPPED,
            sampleProductOrderItem = ProductOrderItemSummary(
                productOrderItemId = UUID.randomUUID(),
                productVariant = ProductVariantSummary(
                    productVariantId = UUID.randomUUID(),
                    code = 54321,
                    variantPrice = BigDecimal("150.50"),
                    variantName = "Biały T-shirt, rozmiar L"
                ),
                unitPrice = BigDecimal("150.50"),
                amount = 1,
                productName = "\"Koszulka, Zielona\""
            ),
            totalDue = BigDecimal("150.50"),
            trackingNumber = "\"DEF987654321\"",
            timeOfSending = LocalDateTime.parse("2024-12-01T15:45:30.123"),
            timeOfCreation = LocalDateTime.parse("2024-12-01T12:00:00.000")
        )
    )
}