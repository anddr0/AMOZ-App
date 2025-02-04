package com.example.amoz.ui.screens.bottom_screens.home

import OrderListItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.AppPreferences
import com.example.amoz.ui.components.EmptyLayout
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.view_models.OrdersViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun LastOrdersList(
    ordersViewModel: OrdersViewModel,
    maxListItemsVisible: Int
) {
    val orderListUiState by ordersViewModel.ordersUiState.collectAsState()
    val stateView = orderListUiState.ordersListFetched
    val currency by ordersViewModel.getCurrency().collectAsState(initial = "USD")

    ResultStateView(
        state = stateView,
        onPullToRefresh = {
            ordersViewModel.fetchOrdersList(skipLoading = true) {}
        }
    ) {
        if (orderListUiState.ordersList.isEmpty()) {
            EmptyLayout()
        } else {
            val sortedOrdersList = orderListUiState.ordersList
                .sortedByDescending { it.timeOfCreation }
                .take(maxListItemsVisible)

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                sortedOrdersList.forEach { order ->
                    OrderListItem(
                        order = order,
                        currency = currency!!,
                        ordersViewModel = ordersViewModel
                    )
                }
            }
        }
    }
}