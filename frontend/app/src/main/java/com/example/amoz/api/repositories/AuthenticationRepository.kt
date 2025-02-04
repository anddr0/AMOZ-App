package com.example.amoz.api.repositories

import android.content.Context
import com.example.amoz.R
import com.example.amoz.api.responses.AuthTokenResponse
import com.example.amoz.api.services.AuthenticationService
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import retrofit2.Response
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val authenticationService: AuthenticationService
) : BaseRepository() {
    suspend fun getTokens(authCode: String): AuthTokenResponse? {
        return performRequest {
            authenticationService.getTokens(authCode)
        }
    }

    suspend fun refreshAccessToken(refreshToken: String): AuthTokenResponse? {
        return performRequest {
            authenticationService.refreshAccessToken(refreshToken)
        }
    }
}
