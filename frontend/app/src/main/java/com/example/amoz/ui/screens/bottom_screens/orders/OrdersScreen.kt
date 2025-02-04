package com.example.amoz.ui.screens.bottom_screens.orders

import FilteredOrdersList
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.ui.components.filters.OrdersFilterBottomSheet
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.OrdersViewModel

@Composable
fun OrdersScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    ordersViewModel: OrdersViewModel,
) {

    LaunchedEffect(true) {
        ordersViewModel.clearCurrentAddEditOrderState()
        ordersViewModel.fetchOrdersListOnScreenLoad()
    }

    val ordersUiState by ordersViewModel.ordersUiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        FilteredOrdersList(
            onMoreFiltersClick = {ordersViewModel.changeFilterBottomSheetStatus(true)},
            onOrderEdit = {orderId ->
                ordersViewModel.updateCurrentAddEditOrder(orderId)
                navController.navigate(Screens.OrdersAddEdit.route)
            },
            onOrderRemove = {orderId ->
                ordersViewModel.removeProductOrder(orderId)
            },
            onGenerateInvoice = {orderId ->
                ordersViewModel.generateProductOrderInvoice(orderId)
            },
            ordersViewModel = ordersViewModel,
        )

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    ordersViewModel.updateCurrentAddEditOrder(null)
                    navController.navigate(Screens.OrdersAddEdit.route)
                },
                modifier = Modifier
                    .padding(16.dp),
                icon = { Icon(Icons.Filled.Add, "Sale add button.") },
                text = { Text(text = stringResource(R.string.add_new)) }
            )
        }

        if (ordersUiState.isFilterBottomSheetExpanded) {
            OrdersFilterBottomSheet(
                onDismissRequest = { ordersViewModel.changeFilterBottomSheetStatus(false) },
                onCancelFilters = { ordersViewModel.cancelFilterParams() },
                filterParams = ordersUiState.filterParams,
                onApplyFilters = { ordersViewModel.saveFilterParams(it) }
            )
        }
    }
}






