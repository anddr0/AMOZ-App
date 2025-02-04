package com.example.amoz.api.managers

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONObject
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Base64
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val invalidationTime: LocalDateTime get() {
        return try {
            LocalDateTime.parse(sharedPreferences.getString("INVALIDATION_TIME", null))
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    val accessTokenTTLSeconds: Int get() {
        val currentTime = LocalDateTime.now()
        val duration = Duration.between(currentTime, invalidationTime)
        return duration.toSeconds().toInt()
    }

    val accessToken: String? get() {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        with(sharedPreferences.edit()) {
            if (accessToken != null) {
                setInvalidationTime(LocalDateTime.now().plusHours(1))
                putString("ACCESS_TOKEN", accessToken)
            }
            if (refreshToken != null) {
                putString("REFRESH_TOKEN", refreshToken)
            }
            apply()
        }
    }

    private fun setInvalidationTime(time: LocalDateTime) {
        with(sharedPreferences.edit()) {
            putString("INVALIDATION_TIME", time.toString())
            apply()
        }
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("REFRESH_TOKEN", null)
    }

    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove("ACCESS_TOKEN")
            remove("REFRESH_TOKEN")
            remove("INVALIDATION_TIME")
            apply()
        }
    }
}
