package com.example.amoz.api.repositories

import com.example.amoz.api.requests.UserRegisterRequest
import com.example.amoz.api.services.UserService
import javax.inject.Inject
import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.api.managers.FirebaseManager
import com.example.amoz.extensions.toImageBitmap
import okhttp3.MultipartBody

class UserRepository @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val userService: UserService
) : BaseRepository() {
    suspend fun registerUser(request: UserRegisterRequest): com.example.amoz.models.User? {
        return performRequest {
            userService.registerUser(request)
        }
    }

    suspend fun updateUser(request: UserRegisterRequest): com.example.amoz.models.User? {
        return performRequest {
            userService.updateUser(request)
        }
    }

    suspend fun uploadProfilePicture(file: MultipartBody.Part) {
        performRequest {
            userService.uploadProfilePicture(file)
        }
    }

    suspend fun getProfilePicture(): ImageBitmap? {
        return performRequest {
            userService.getProfilePicture()
        }.toImageBitmap()
    }

    suspend fun updatePushToken() {
        firebaseManager.getDeviceToken()
            ?.let { pushToken ->
                performRequest {
                    userService.updatePushToken(pushToken)
                }
            }
    }

    suspend fun isUserRegistered(): Boolean {
        return performRequest {
            userService.isUserRegistered()
        }?.let {
            return it.isRegistered
        } ?: false
    }
}
