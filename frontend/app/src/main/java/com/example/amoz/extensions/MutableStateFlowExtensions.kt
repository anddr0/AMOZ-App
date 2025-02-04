package com.example.amoz.extensions

import com.example.amoz.api.sealed.ResultState
import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<ResultState<T>>.updateResultState(function: (T) -> T): ResultState<T>?  {
    this.fetchDataIfSuccess()?.let {
        val updatedData = function(it)
        this.value = ResultState.Success(updatedData)
        return ResultState.Success(updatedData)
    }
    return null
}

fun <T> MutableStateFlow<ResultState<T>>.fetchDataIfSuccess(): T? {
    val state = this.value
    return if (state is ResultState.Success) {
        state.data
    } else {
        null
    }
}

