import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.AppPreferences
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.text_fields.SearchTextField
import com.example.amoz.ui.screens.bottom_screens.orders.orders_list.FilterChipsOrders
import com.example.amoz.view_models.OrdersViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import androidx.compose.foundation.lazy.items
import com.example.amoz.ui.components.EmptyLayout
import com.example.amoz.ui.components.list_items.OrderGroupHeader
import com.example.amoz.ui.components.list_items.SwipeOrderListItem
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun FilteredOrdersList(
    onMoreFiltersClick: () -> Unit,
    onOrderEdit: (UUID) -> Unit,
    onOrderRemove: (UUID) -> Unit,
    onGenerateInvoice: (UUID) -> Unit,
    ordersViewModel: OrdersViewModel,
) {
    val orderListUiState by ordersViewModel.ordersUiState.collectAsState()
    val currContext = LocalContext.current
    val appPreferences = remember { AppPreferences(currContext) }

    val currency by appPreferences.currency.collectAsState(initial = "USD")

    val stateView: MutableStateFlow<ResultState<List<Any>>> = orderListUiState.ordersListFetched as MutableStateFlow<ResultState<List<Any>>>

    ResultStateView(
        state = stateView,
        onPullToRefresh = {
            ordersViewModel.fetchOrdersList(skipLoading = true){}
        }
    ) { fetchedList ->
        if (fetchedList.isEmpty()){
           EmptyLayout()
        } else {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())

            val groupedOrders = orderListUiState.filteredSortedOrdersList
                .groupBy {
                    it.timeOfCreation.format(dateFormatter)
                }
                .toSortedMap(compareByDescending { it })

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {
                    SearchTextField(
                        searchQuery = orderListUiState.searchQuery,
                        placeholder = stringResource(id = R.string.search_products_placeholder),
                        onSearchQueryChange = {
                            Log.d("SearchQuery",orderListUiState.searchQuery)
                            ordersViewModel.updateSearchQuery(it)
                        },
                        onMoreFiltersClick = onMoreFiltersClick
                    )
                    FilterChipsOrders(
                        priceFrom = orderListUiState.filterParams.priceFrom,
                        onPriceFromClick = { ordersViewModel.clearPriceFilter(true) },
                        priceTo = orderListUiState.filterParams.priceTo,
                        onPriceToClick = { ordersViewModel.clearPriceFilter(false) },
                        status = orderListUiState.filterParams.status,
                        onStatusClick = { ordersViewModel.clearStatusFilter() },
                        timeOfSendingFrom = orderListUiState.filterParams.timeOfSendingFrom,
                        onTimeOfSendingFromClick = { ordersViewModel.clearTimeOfSendingFilter(true) },
                        timeOfSendingTo = orderListUiState.filterParams.timeOfSendingTo,
                        onTimeOfSendingToClick = { ordersViewModel.clearTimeOfSendingFilter(false) },
                        timeOfCreationFrom = orderListUiState.filterParams.timeOfCreationFrom,
                        onTimeOfCreationFromClick = { ordersViewModel.clearTimeOfCreationFilter(true) },
                        timeOfCreationTo = orderListUiState.filterParams.timeOfCreationTo,
                        onTimeOfCreationToClick = { ordersViewModel.clearTimeOfCreationFilter(false) },
                    )
                }

                groupedOrders.forEach { (date, orders) ->
                    item {
                        OrderGroupHeader(date = date)
                    }
                    items(orders) { orderTemplate ->
                        Box(
                            modifier = Modifier.animateItem()
                        ) {
                            SwipeOrderListItem(
                                order = orderTemplate,
                                onOrderEdit = onOrderEdit,
                                onOrderRemove = onOrderRemove,
                                currency = currency,
                                ordersViewModel = ordersViewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}