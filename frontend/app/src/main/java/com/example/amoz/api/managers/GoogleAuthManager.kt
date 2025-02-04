package com.example.amoz.api.managers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.amoz.R
import com.example.amoz.interfaces.SignInDelegate
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

class GoogleAuthManager @Inject constructor(
    private val context: Context,
    private val tokenManager: TokenManager
) {
    var delegate: SignInDelegate? = null

    val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.google_sign_in_client_id))
        .requestEmail()
        .requestServerAuthCode(context.getString(R.string.google_sign_in_client_id), true)
        .build()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun handleSignInResult(task: Task<GoogleSignInAccount>): String {
        return suspendCancellableCoroutine { continuation ->
            task.addOnSuccessListener { account ->
                val idToken = account.idToken
                val authCode = account.serverAuthCode
                authCode?.let {
                    continuation.resume(it, onCancellation = null)
                } ?: run {
                    continuation.resumeWithException(Exception("Null auth code"))
                }
            }
            task.addOnFailureListener { e ->
                if (e is ApiException) {
                    Log.w("GoogleAuthManager", "handleSignInResult: failed code=${e.statusCode}")
                }
                continuation.resumeWithException(e)
            }
        }
    }

    private fun createSignInClient(): GoogleSignInClient {
        return GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return createSignInClient().signInIntent
    }

    fun startSignInActivity(completion: (() -> Unit)?) {
        Log.i("authManager", delegate.toString())
        delegate?.startSignInActivityForResult(completion)
    }

    fun signOut(activity: Activity, completion: (() -> Unit)?) {
        val googleSignInClient = createSignInClient()
        googleSignInClient.signOut()
            .addOnCompleteListener(activity) {
                tokenManager.clearTokens()
                completion?.invoke()
            }
    }
}

