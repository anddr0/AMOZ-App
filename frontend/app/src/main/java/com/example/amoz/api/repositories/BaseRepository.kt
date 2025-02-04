package com.example.amoz.api.repositories

import android.util.Log
import retrofit2.Response

abstract class BaseRepository {
    suspend fun <T> performRequest(request: suspend () -> Response<T>): T? {
        val tag = "${this::class.java.simpleName}.${Thread.currentThread().stackTrace[3].methodName}"
        return try {
            val response: Response<T> = request()
            if (response.isSuccessful) {
                Log.i(tag, "${response.code()} ${response.body()}")
                response.body()
            } else {
                Log.e(tag, "${response.code()} ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(tag, "Exception ${e.message}", e)
            null
        }
    }
}