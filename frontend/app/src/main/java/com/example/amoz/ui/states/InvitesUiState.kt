package com.example.amoz.ui.states

import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductOrderSummary
import kotlinx.coroutines.flow.MutableStateFlow

data class InvitesUiState (
    val invitesListFetched: MutableStateFlow<ResultState<List<ProductOrderSummary>>> = MutableStateFlow(ResultState.Idle),
)