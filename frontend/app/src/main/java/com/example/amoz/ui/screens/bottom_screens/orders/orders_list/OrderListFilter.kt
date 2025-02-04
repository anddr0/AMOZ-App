package com.example.amoz.ui.screens.bottom_screens.orders.orders_list

import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.view_models.OrdersViewModel
import java.math.BigDecimal

class OrderListFilter {
    fun filterOrders(
        templates: List<ProductOrderSummary>,
        searchQuery: String,
        filterParams: OrdersViewModel.OrderFilterParams,
    ): List<ProductOrderSummary> {
        return templates
            .filter { order ->
                (searchQuery.takeIf { it.isNotBlank() }?.let {
                    order.sampleProductOrderItem.productName.contains(it, ignoreCase = true) ||
                            order.sampleProductOrderItem.productVariant.variantName?.contains(it, ignoreCase = true) ?: true
                } ?: true)
                &&
                (filterParams.status?.let {
                    order.status == it
                } ?: true)
                &&
                (order.totalDue in
                        (filterParams.priceFrom ?: BigDecimal.ZERO)..
                        (filterParams.priceTo ?: BigDecimal(Int.MAX_VALUE)))
                && (filterParams.timeOfCreationFrom?.let {
                    order.timeOfCreation >= it
                } ?: true)
                && (filterParams.timeOfCreationTo?.let {
                    order.timeOfCreation <= it
                } ?: true)
                && (filterParams.timeOfSendingFrom?.let {
                    order.timeOfSending?.let { timeOfSending ->
                        timeOfSending >= it
                    } ?: false
                } ?: true)
                && (filterParams.timeOfSendingTo?.let {
                    order.timeOfSending?.let { timeOfSending ->
                        timeOfSending <= it
                    } ?: false
                } ?: true)

            }
            .sortedWith(
                when (filterParams.sortingType) {
                    OrdersViewModel.SortingType.ASCENDING_PRICE -> compareBy { it.totalDue }
                    OrdersViewModel.SortingType.DESCENDING_PRICE -> compareByDescending { it.totalDue }
                    OrdersViewModel.SortingType.ASCENDING_TIME_OF_SENDING -> compareBy { it.timeOfSending }
                    OrdersViewModel.SortingType.DESCENDING_TIME_OF_SENDING -> compareByDescending { it.timeOfSending }
                    OrdersViewModel.SortingType.ASCENDING_TIME_OF_CREATION -> compareBy { it.timeOfCreation }
                    OrdersViewModel.SortingType.DESCENDING_TIME_OF_CREATION -> compareByDescending { it.timeOfCreation }
                    OrdersViewModel.SortingType.NONE -> compareBy { it.productOrderId } // Default or no sorting
                }
            )
    }
}