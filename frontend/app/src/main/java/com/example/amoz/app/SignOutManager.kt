package com.example.amoz.app

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SignOutManager {
    private val _signOutEvent = MutableSharedFlow<Unit>(replay = 0)
    val signOutEvent: SharedFlow<Unit> = _signOutEvent

    suspend fun notifySignOut() {
        _signOutEvent.emit(Unit)
    }
}