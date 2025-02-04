package com.example.amoz.api.services

import com.example.amoz.models.Attribute
import com.example.amoz.api.requests.AttributeCreateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

import kotlinx.serialization.json.JsonElement
import retrofit2.Response


interface AttributeService {

    @GET("api/attributes")
    suspend fun getAllAttributes(): Response<List<Attribute>>

    @GET("api/attributes/product")
    suspend fun getProductAttributes(): Response<List<Attribute>>

    @GET("api/attributes/variant")
    suspend fun getVariantAttributes(): Response<List<Attribute>>
}

