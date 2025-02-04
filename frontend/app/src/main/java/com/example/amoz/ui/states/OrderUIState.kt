package com.example.amoz.ui.states
import androidx.compose.ui.graphics.ImageBitmap
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.models.ProductSummary
import com.example.amoz.view_models.OrdersViewModel.OrderFilterParams
import com.example.amoz.view_models.OrdersViewModel.ProductVariantOrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal
import java.util.UUID

data class OrderUIState (
    val ordersListFetched: MutableStateFlow<ResultState<List<ProductOrderSummary>>> = MutableStateFlow(ResultState.Idle),

    val ordersList: List<ProductOrderSummary> = emptyList(),
    val amountOfItems: Int = 0,
    val itemsSumPrice: BigDecimal = BigDecimal.ZERO,
    val amountOfItemsNewStatus: Int = 0,

    val productsList: List<ProductSummary> = listOf(),
    val productVariantsImagesMap: Map<UUID, ImageBitmap?> = emptyMap(),

    val searchQuery: String = "",
    val filterParams: OrderFilterParams = OrderFilterParams(),
    val filteredSortedOrdersList: List<ProductOrderSummary> = ordersList,

    // Details of the order being edited
    // currently being set to null at orders screen initialization
    val currentInvoicePDFByteArray: MutableStateFlow<ResultState<ByteArray>> = MutableStateFlow(ResultState.Idle),
    val currentAddEditOrderDetails: ProductOrderDetails? = null,
    val currentAddEditOrderState: MutableStateFlow<ResultState<ProductOrderCreateRequest>> = MutableStateFlow(ResultState.Idle),
    val currentCustomerDetails: CustomerAnyRepresentation? = null,
    val currentProductVariantDetailsList: List<ProductVariantOrderItem> = emptyList(),
    val isCurrentOrderNew: Boolean = true,
    val currentOrderProductVariantsImagesMap: Map<UUID, ImageBitmap?> = emptyMap(),
    val currentOrderTotalPrice: BigDecimal = BigDecimal.ZERO,


    val showOrdersList: Boolean = true,
    val isFilterBottomSheetExpanded: Boolean = false,
    val isAddressBottomSheetExpanded: Boolean = false,
    val isDropdownStatusExpanded: Boolean = false,
)