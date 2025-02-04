package com.example.amoz.interfaces

interface SignInDelegate {
    fun startSignInActivityForResult(completion: (() -> Unit)? = null)
}