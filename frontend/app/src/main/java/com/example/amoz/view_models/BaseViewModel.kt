package com.example.amoz.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.api.sealed.SyncResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class BaseViewModel: ViewModel() {
    protected val tag: String
        get() = "${this::class.java.simpleName}.${Thread.currentThread().stackTrace[3].methodName}"

    fun <T> performRepositoryAction(binding: MutableStateFlow<ResultState<T>>?,
                                    failureMessage: String = "Please try again later",
                                    skipLoading: Boolean = false,
                                    action: suspend () -> T?,
                                    onSuccess: ((T) -> Unit)? = null,
                                    onFailure: ((String) -> Unit)? = null) {
        if (!skipLoading) {
            binding?.value = ResultState.Loading
        }
        viewModelScope.launch {
            try {
                val response = action()
                if (response != null) {
                    Log.i(tag, response.toString())
                    binding?.value = ResultState.Success(response)
                    onSuccess?.invoke(response)
                } else {
                    binding?.value = ResultState.Failure(failureMessage)
                    onFailure?.invoke(failureMessage)
                }
            } catch (e: Exception) {
                Log.e(tag, "An exception has occurred: ${e.message}", e)
                binding?.value = ResultState.Failure("Please try again later")
            }
        }
    }

    fun <T> assertModelIsValid(binding: StateFlow<SyncResultState<T>>, action: (T) -> Unit) {
        when (val state = binding.value) {
            is SyncResultState.Success -> {
                action(state.data)
            }
            is SyncResultState.Failure -> {
                Log.w(tag, state.message)
            }
            is SyncResultState.Idle -> {
                throw IllegalStateException()
            }
        }
    }
}