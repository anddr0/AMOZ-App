package com.example.amoz.api.services

import com.example.amoz.models.Company
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.responses.MessageResponse
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface CompanyService {

    @POST("api/companies")
    suspend fun createCompany(@Body request: CompanyCreateRequest): Response<Company>

    @GET("api/companies")
    suspend fun getUserCompany(): Response<Company>

    @PUT("api/companies")
    suspend fun updateCompany(@Body request: CompanyCreateRequest): Response<Company>

    @PATCH("api/companies")
    suspend fun deactivateCompany(): Response<Unit>

    @Multipart
    @PUT("api/companies/picture")
    suspend fun uploadCompanyProfilePicture(@Part file: MultipartBody.Part): Response<Unit>

    @GET("api/companies/picture")
    suspend fun getCompanyProfilePicture(): Response<ResponseBody>
}
