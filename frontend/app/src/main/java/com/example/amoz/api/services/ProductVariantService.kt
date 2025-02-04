package com.example.amoz.api.services

import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface ProductVariantService {

    @POST("api/productVariants")
    suspend fun createProductVariant(
        @Body request: ProductVariantCreateRequest
    ): Response<ProductVariantDetails>

    @PUT("api/productVariants/{productVariantId}")
    suspend fun updateProductVariant(
        @Path("productVariantId") productVariantId: UUID,
        @Body request: ProductVariantCreateRequest
    ): Response<ProductVariantDetails>

    @PATCH("api/productVariants/{productVariantId}")
    suspend fun deactivateProductVariant(
        @Path("productVariantId") productVariantId: UUID
    ): Response<Unit>

    @GET("api/productVariants/product/{productId}")
    suspend fun getAllProductVariantsByProductId(
        @Path("productId") productId: UUID
    ): Response<List<ProductVariantSummary>>

    @GET("api/productVariants/{productVariantId}")
    suspend fun getProductVariant(
        @Path("productVariantId") productVariantId: UUID
    ): Response<ProductVariantDetails>

    @Multipart
    @PUT("api/productVariants/picture/{productVariantId}")
    suspend fun uploadProductVariantPicture(
        @Path("productVariantId") productVariantId: UUID,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @GET("api/productVariants/picture/{productVariantId}")
    suspend fun getProductVariantPicture(
        @Path("productVariantId") productVariantId: UUID
    ): Response<ResponseBody>
}