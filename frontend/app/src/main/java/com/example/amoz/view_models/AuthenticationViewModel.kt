package com.example.amoz.view_models

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.amoz.interfaces.SignInDelegate
import com.example.amoz.api.managers.GoogleAuthManager
import com.example.amoz.api.managers.TokenManager
import com.example.amoz.api.repositories.AuthenticationRepository
import com.example.amoz.app.SignOutManager
import com.example.amoz.ui.screens.Screens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager,
    private val signOutManager: SignOutManager,
    private val tokenManager: TokenManager,
    private val authenticationRepository: AuthenticationRepository,
) : BaseViewModel() {

    fun setSignInDelegate(delegate: SignInDelegate) {
        googleAuthManager.delegate = delegate
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>, completion: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val authCode = googleAuthManager.handleSignInResult(task)
                authenticationRepository.getTokens(authCode)?.let {
                    tokenManager.saveTokens(it.accessToken, it.refreshToken)
                    Log.d("access_token", tokenManager.accessToken ?: "null token")
                    completion?.invoke()
                }
            } catch(e: Exception) {
                Log.w("AuthenticationViewModel", e)
            }
        }
    }

//
    fun getSignInIntent(activity: Activity): Intent {
        return googleAuthManager.getSignInIntent()
    }

    fun signOut(activity: Activity, completion: (() -> Unit)?) {
        googleAuthManager.signOut(activity) {
            viewModelScope.launch {
                signOutManager.notifySignOut()
            }
            completion?.invoke()
        }
    }

    fun isSignedIn(): Boolean {
        return tokenManager.getRefreshToken() != null
    }

    fun isSignedInRedirect(navController: NavHostController, onSuccess: (Boolean) -> Unit) {
        if (!isSignedIn()) {
            navController.navigate(Screens.Entry.route){
                popUpTo(0){inclusive = true}
            }
        } else {
            onSuccess(true)
        }
    }
}
