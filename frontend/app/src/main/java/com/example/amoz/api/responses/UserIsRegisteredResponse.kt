package com.example.amoz.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserIsRegisteredResponse(

    val isRegistered: Boolean
)