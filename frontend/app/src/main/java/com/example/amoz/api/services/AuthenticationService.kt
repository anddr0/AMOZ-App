package com.example.amoz.api.services

import com.example.amoz.api.responses.AuthTokenResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationService {

    @GET("api/authentication/token")
    suspend fun getTokens(@Query("authCode") authCode: String): Response<AuthTokenResponse>

    @POST("api/authentication/refreshToken")
    suspend fun refreshAccessToken(@Query("refreshToken") refreshToken: String): Response<AuthTokenResponse>
}
