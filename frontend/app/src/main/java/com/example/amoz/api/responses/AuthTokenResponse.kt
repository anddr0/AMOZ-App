package com.example.amoz.api.responses

import kotlinx.serialization.Serializable


@Serializable
data class AuthTokenResponse(

    val accessToken: String,

    val refreshToken: String
)
