package com.example.amoz.api.sealed

sealed class SyncResultState<out T> {
    data object Idle : SyncResultState<Nothing>()

    data class Success<out T>(val data: T) : SyncResultState<T>()

    data class Failure(val message: String) : SyncResultState<Nothing>()
}
