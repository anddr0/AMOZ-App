package com.example.amoz.api.services

import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.responses.MessageResponse
import com.example.amoz.models.InvoiceSummary
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface ProductOrderService {

    @POST("api/productOrders")
    suspend fun createProductOrder(
        @Body request: ProductOrderCreateRequest
    ): Response<ProductOrderDetails>

    @PUT("api/productOrders/{productOrderId}")
    suspend fun updateProductOrder(
        @Path("productOrderId") productOrderId: UUID,
        @Body request: ProductOrderCreateRequest
    ): Response<ProductOrderDetails>

    @PUT("api/productOrders/{productOrderId}/generateInvoice")
    suspend fun generateInvoice(
        @Path("productOrderId") productOrderId: UUID
    ): Response<InvoiceSummary>

    @POST("api/productOrders/invoice/{invoiceId}")
    suspend fun sendInvoiceEmail(
        @Path("invoiceId") invoiceId: UUID
    ): Response<MessageResponse>

    @DELETE("api/productOrders/{productOrderId}")
    suspend fun removeProductOrder(
        @Path("productOrderId") productOrderId: UUID
    ): Response<Unit>

    @GET("api/productOrders/invoice/{invoiceId}")
    suspend fun downloadInvoicePDF(
        @Path("invoiceId") invoiceId: UUID
    ): Response<ResponseBody>

    @GET("api/productOrders/{productOrderId}")
    suspend fun getProductOrderDetails(
        @Path("productOrderId") productOrderId: UUID
    ): Response<ProductOrderDetails>

    @GET("api/productOrders")
    suspend fun getAllProductOrders(): Response<List<ProductOrderSummary>>
}
