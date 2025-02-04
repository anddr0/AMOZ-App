package com.example.amoz.api.services

import com.example.amoz.models.CustomerB2B
import com.example.amoz.models.CustomerB2C
import com.example.amoz.api.requests.CustomerB2BCreateRequest
import com.example.amoz.api.requests.CustomerB2CCreateRequest
import kotlinx.serialization.json.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

import retrofit2.Response
import retrofit2.http.*

interface CustomerService {
    @GET("api/customers/{customerId}")
    suspend fun getCustomerDetails(@Path("customerId") customerId: UUID): Response<JsonElement>

    @POST("api/customers/b2b")
    suspend fun createCustomerB2B(@Body request: CustomerB2BCreateRequest): Response<CustomerB2B>

    @POST("api/customers/b2c")
    suspend fun createCustomerB2C(@Body request: CustomerB2CCreateRequest): Response<CustomerB2C>

    @PUT("api/customers/b2b/{customerId}")
    suspend fun updateCustomerB2B(
        @Path("customerId") customerId: UUID,
        @Body request: CustomerB2BCreateRequest
    ): Response<CustomerB2B>

    @PUT("api/customers/b2c/{customerId}")
    suspend fun updateCustomerB2C(
        @Path("customerId") customerId: UUID,
        @Body request: CustomerB2CCreateRequest
    ): Response<CustomerB2C>

    @GET("api/customers/b2b")
    suspend fun getAllCustomersB2B(): Response<List<CustomerB2B>>

    @GET("api/customers/b2c")
    suspend fun getAllCustomersB2C(): Response<List<CustomerB2C>>
}


