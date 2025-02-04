package com.example.amoz.api.repositories

import android.util.Log
import com.example.amoz.R
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.responses.MessageResponse
import com.example.amoz.api.services.ProductOrderService
import com.example.amoz.extensions.toByteArray
import com.example.amoz.models.InvoiceSummary
import kotlinx.serialization.json.JsonElement
import java.util.UUID
import javax.inject.Inject

class ProductOrderRepository @Inject constructor(
    private val productOrderService: ProductOrderService
) : BaseRepository() {

    suspend fun createProductOrder(request: ProductOrderCreateRequest): ProductOrderDetails? {
        return performRequest {
            productOrderService.createProductOrder(request)
        }
    }

    suspend fun updateProductOrder(productOrderId: UUID, request: ProductOrderCreateRequest): ProductOrderDetails? {
        Log.d("ProductOrderRepository", "updateProductOrder: $productOrderId")
        Log.d("ProductOrderRepository", "updateProductOrder: $request")
        return performRequest {
            productOrderService.updateProductOrder(productOrderId, request)
        }
    }

    suspend fun generateInvoice(productOrderId: UUID): InvoiceSummary? {
        return performRequest {
            productOrderService.generateInvoice(productOrderId)
        }
    }

    suspend fun sendInvoiceEmail(invoiceId: UUID): MessageResponse? {
        return performRequest {
            productOrderService.sendInvoiceEmail(invoiceId)
        }
    }

    suspend fun removeProductOrder(productOrderId: UUID) {
        performRequest {
            productOrderService.removeProductOrder(productOrderId)
        }
    }

    suspend fun downloadInvoicePDF(invoiceId: UUID): ByteArray? {
        return performRequest {
            productOrderService.downloadInvoicePDF(invoiceId)
        }.toByteArray()
    }

    suspend fun getProductOrderDetails(productOrderId: UUID): ProductOrderDetails? {
        return performRequest {
            productOrderService.getProductOrderDetails(productOrderId)
        }
    }

    suspend fun getAllProductOrders(): List<ProductOrderSummary> {
        return performRequest {
            productOrderService.getAllProductOrders()
        } ?: listOf()
    }
}
