package com.example.amoz.api.managers

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseManager @Inject constructor() {
    suspend fun getDeviceToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            throw RuntimeException("Nie udało się pobrać tokenu FCM: ${e.message}")
        }
    }
}
