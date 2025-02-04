package com.example.amoz.api.services

import com.example.amoz.models.User
import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.responses.MessageResponse
import com.example.amoz.api.responses.UserIsRegisteredResponse
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @POST("api/users")
    suspend fun registerUser(@Body request: UserRegisterRequest): Response<User>

    @PUT("api/users")
    suspend fun updateUser(@Body request: UserRegisterRequest): Response<User>

    @Multipart
    @PUT("api/users/picture")
    suspend fun uploadProfilePicture(@Part file: MultipartBody.Part): Response<Unit>

    @GET("api/users/picture")
    suspend fun getProfilePicture(): Response<ResponseBody>

    @GET("api/users/isRegistered")
    suspend fun isUserRegistered(): Response<UserIsRegisteredResponse>

    @POST("api/users/pushToken")
    suspend fun updatePushToken(@Query("token") token: String): Response<Unit>

}
