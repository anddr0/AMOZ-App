package com.example.amoz.api.services

import com.example.amoz.models.Employee
import com.example.amoz.models.Invitation
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface EmployeeService {

    @POST("api/employees/acceptInvitation")
    suspend fun acceptInvitationToCompany(@Query("token") token: String): Response<Unit>

    @POST("api/employees/rejectInvitation")
    suspend fun rejectInvitationToCompany(@Query("token") token: String): Response<Unit>

    @POST("api/employees/invite")
    suspend fun inviteEmployeeToCompany(
        @Query("employeeEmail") employeeEmail: String
    ): Response<Unit>

    @PATCH("api/employees/kick/{employeeId}")
    suspend fun kickEmployeeFromCompany(
        @Path("employeeId") employeeId: UUID
    ): Response<Unit>

    @PATCH("api/employees/leave")
    suspend fun leaveCompany(): Response<Unit>

    @GET("api/employees")
    suspend fun fetchEmployees(): Response<List<Employee>>

    @GET("api/employees/invitations")
    suspend fun fetchInvitations(): Response<List<Invitation>>

    @GET("api/employees/employee")
    suspend fun fetchEmployee(): Response<Employee>

    @GET("api/employees/picture/{employeeId}")
    suspend fun getEmployeePicture(
        @Path("employeeId") employeeId: UUID
    ): Response<ResponseBody>
}
