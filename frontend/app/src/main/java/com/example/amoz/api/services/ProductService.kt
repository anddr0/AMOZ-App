package com.example.amoz.api.services

import com.example.amoz.api.requests.ProductCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ProductService {

    @POST("api/products")
    suspend fun createProduct(
        @Body request: ProductCreateRequest
    ): Response<com.example.amoz.models.ProductDetails>

    @PUT("api/products/{productId}")
    suspend fun updateProduct(
        @Path("productId") productId: UUID,
        @Body request: ProductCreateRequest
    ): Response<com.example.amoz.models.ProductDetails>

    @PATCH("api/products/{productId}/mainVariant/{mainVariantId}")
    suspend fun setMainVariant(
        @Path("productId") productId: UUID,
        @Path("mainVariantId") mainVariantId: UUID
    ): Response<com.example.amoz.models.ProductDetails>

    @PATCH("api/products/{productId}")
    suspend fun deactivateProduct(
        @Path("productId") productId: UUID
    ): Response<Unit>

    @GET("api/products")
    suspend fun getAllProducts(): Response<List<com.example.amoz.models.ProductSummary>>

    @GET("api/products/{productId}")
    suspend fun getProductDetails(
        @Path("productId") productId: UUID
    ): Response<com.example.amoz.models.ProductDetails>
}
