package com.example.amoz.ui.components

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.amoz.api.sealed.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ResultStateView(
    state: MutableStateFlow<ResultState<T>>,
    modifier: Modifier = Modifier,
    onPullToRefresh: (() -> Unit)? = null,
    loadingView: (@Composable () -> Unit)? = null,
    failureView: @Composable ((String) -> Unit)? = null,
    successView: (@Composable (T) -> Unit)? = null
) {

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            onPullToRefresh?.invoke()
            delay(1000)
            isRefreshing = false
        }
    }

    if (onPullToRefresh != null) {
        PullToRefreshBox (
            modifier = modifier,
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ) {
            ContentStateView(state, loadingView, failureView, successView)
        }
    }
    else {
        Box(
            modifier = modifier,
        ) {
            ContentStateView(state, loadingView, failureView, successView)
        }
    }
}

@Composable
fun <T> ContentStateView(
    state: MutableStateFlow<ResultState<T>>,
    loadingView: (@Composable () -> Unit)? = null,
    failureView: @Composable ((String) -> Unit)? = null,
    successView: (@Composable (T) -> Unit)? = null
) {
    val context = LocalContext.current

    when (val resultState = state.collectAsState().value) {
        is ResultState.Idle -> {
            Unit
        }
        is ResultState.Loading -> {
            loadingView ?: LoadingView()
        }

        is ResultState.Failure -> {
            if (failureView != null) {
                failureView.invoke(resultState.message)
            } else {
                LaunchedEffect(resultState.message) {
                    Toast.makeText(context, resultState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        is ResultState.Success -> {
            successView?.invoke(resultState.data)
        }
    }
}