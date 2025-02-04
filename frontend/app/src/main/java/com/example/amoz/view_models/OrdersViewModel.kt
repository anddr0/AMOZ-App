package com.example.amoz.view_models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.example.amoz.R
import com.example.amoz.api.enums.Status
import com.example.amoz.api.repositories.CustomerRepository
import com.example.amoz.api.repositories.ProductOrderRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.requests.ProductOrderItemCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.AppPreferences
import com.example.amoz.app.SignOutManager
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.models.ProductOrderDetails
import com.example.amoz.models.ProductOrderItemSummary
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.screens.bottom_screens.orders.orders_list.OrderListFilter
import com.example.amoz.ui.states.CompanyUIState
import com.example.amoz.ui.states.OrderUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor (
    @ApplicationContext val context: Context,
    private val orderRepository: ProductOrderRepository,
    private val productRepository: ProductVariantRepository,
    private val customerRepository: CustomerRepository,
    private val appPreferences: AppPreferences,
    private val signOutManager: SignOutManager,
): BaseViewModel() {

    private val _ordersUiState = MutableStateFlow(OrderUIState())
    val ordersUiState: StateFlow<OrderUIState> = _ordersUiState.asStateFlow()
    private val _addressCreateRequestState = MutableStateFlow(
        AddressCreateRequest()
    )
    private val orderFilter = OrderListFilter()

    init {
        observeSignOutEvent()
    }

    private fun observeSignOutEvent() {
        viewModelScope.launch {
            signOutManager.signOutEvent.collect {
                clearState()
            }
        }
    }

    private fun clearState() {
        _ordersUiState.update { OrderUIState() }
        _addressCreateRequestState.value = AddressCreateRequest()
    }

    // --------------------------------

    fun getCurrency(): Flow<String?> {
        return appPreferences.currency
    }

    // --------------------------------

    fun changeFilterBottomSheetStatus(state: Boolean) {
        _ordersUiState.update { it.copy(isFilterBottomSheetExpanded = state) }
    }

    fun changeAddressBottomSheetStatus(state: Boolean) {
        _ordersUiState.update { it.copy(isAddressBottomSheetExpanded = state) }
    }

    // --------------------------------

    fun createOrderRequestFromVariantItems(
        listVariantOrderItem: List<ProductVariantOrderItem>,
        orderCreateRequest: ProductOrderCreateRequest
    ): ProductOrderCreateRequest {
        val productOrderItems = listVariantOrderItem.map {
            ProductOrderItemCreateRequest(
                productVariantId = it.productVariant.productVariantId,
                amount = it.quantity
            )
        }

        return orderCreateRequest.copy(productOrderItems = productOrderItems)
    }


     fun createProductOrder(orderCreateRequest: ProductOrderCreateRequest,
                            listVariantOrderItem: List<ProductVariantOrderItem>,
                            onSuccessCallback: (ProductOrderDetails) -> Unit) {
        val finalOrderRequest = createOrderRequestFromVariantItems(
            listVariantOrderItem = listVariantOrderItem,
            orderCreateRequest = orderCreateRequest,
        )

        val validationMessage = finalOrderRequest.validate()
        if (validationMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Could not create product order, try again",
                action = { orderRepository.createProductOrder(finalOrderRequest) },
                onSuccess = {
                    onSuccessCallback(it)
                    fetchOrdersList(skipLoading = false) {}
                }
            )
        }
        else {
            Log.e("createProductOrder", validationMessage)
            throw IllegalArgumentException(validationMessage)
        }
    }

    fun saveCurrentAddEditOrderState(orderCreateRequest: ProductOrderCreateRequest,
                                     listVariantOrderItem: List<ProductVariantOrderItem>,
                                     currentCustomerDetails: CustomerAnyRepresentation?,
                                     totalPrice: BigDecimal,
    ) {
        val finalOrderRequest = createOrderRequestFromVariantItems(
            listVariantOrderItem = listVariantOrderItem,
            orderCreateRequest = orderCreateRequest,
        )

        _ordersUiState.update { it.copy(
            currentAddEditOrderState = MutableStateFlow(ResultState.Success(finalOrderRequest)),
            currentProductVariantDetailsList = listVariantOrderItem,
            currentCustomerDetails = currentCustomerDetails,
            currentOrderTotalPrice = totalPrice,
        ) }
    }

    fun clearCurrentAddEditOrderState() {
        _ordersUiState.update { it.copy(
            currentAddEditOrderState = MutableStateFlow(ResultState.Idle),
            currentProductVariantDetailsList = emptyList(),
            currentOrderProductVariantsImagesMap = emptyMap(),
            currentAddEditOrderDetails = null,
            currentCustomerDetails = null,
            isCurrentOrderNew = true,
            currentOrderTotalPrice = BigDecimal.ZERO,
        ) }
    }

    private fun updateProductOrder(uuid: UUID,
                                   currentProductVariantDetailsList: List<ProductVariantOrderItem>,
                                   orderCreateRequest: ProductOrderCreateRequest) {
        val finalOrderRequest = createOrderRequestFromVariantItems(
            listVariantOrderItem = currentProductVariantDetailsList,
            orderCreateRequest = orderCreateRequest,
        )

        val validationMessage = finalOrderRequest.validate()
        if (validationMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Could not update product order, try again",
                action = { orderRepository.updateProductOrder(uuid, orderCreateRequest) },
                onSuccess = {
                    fetchOrdersList(skipLoading = true){}
                }
            )
        }
        else {
            Log.e("updateProductOrder", validationMessage)
            throw IllegalArgumentException(validationMessage)
        }
    }

    fun removeProductOrder(orderId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not remove order, try again",
            action = { orderRepository.removeProductOrder(orderId) },
            onSuccess = {
                fetchOrdersList(skipLoading = true){}
            }
        )
    }

    fun generateProductOrderInvoice(orderId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not generate invoice, try again",
            action = { orderRepository.generateInvoice(orderId)  },
            onSuccess = {
                downloadProductOrderInvoicePDF(it.invoiceId)
                sendProductOrderInvoice(it.invoiceId)
            }
        )
    }

    private fun sendProductOrderInvoice(invoiceId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not send invoice, try again",
            action = { orderRepository.sendInvoiceEmail(invoiceId) },
            onSuccess = {
                Toast.makeText(context, "Invoice sent", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun downloadProductOrderInvoicePDF(invoiceId: UUID) {
        performRepositoryAction(
            binding = _ordersUiState.value.currentInvoicePDFByteArray,
            failureMessage = "Could not download invoice, try again",
            action = {
                orderRepository.downloadInvoicePDF(invoiceId)
            },
            onSuccess = {
                openInvoicePDF(it)
            }
        )
    }

    private fun openInvoicePDF(pdfData: ByteArray) {
        Log.d("openInvoicePDF", "PDF array: $pdfData")
        val fileName = "invoice.pdf"
        val file = File(context.cacheDir, fileName)

        try {
            file.outputStream().use { it.write(pdfData) }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            return
        }

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    fun chooseProductOrderOperation(orderCreateRequest: ProductOrderCreateRequest, currentProductVariantDetailsList: List<ProductVariantOrderItem>) {
        if (_ordersUiState.value.isCurrentOrderNew) {
            createProductOrder(orderCreateRequest, currentProductVariantDetailsList){}
        }
        else {
            val uuid = _ordersUiState.value.currentAddEditOrderDetails?.productOrderId
            updateProductOrder(uuid!!, currentProductVariantDetailsList, orderCreateRequest)
        }
    }

    fun updateAddress(request: AddressCreateRequest?) {
        request?.let {
            _addressCreateRequestState.update { it }
        }
    }

    fun updateCurrentAddEditOrder(orderId: UUID?) {
        if (orderId == null) {
            _ordersUiState.update { it.copy(
                currentAddEditOrderDetails = null,
            ) }
            _ordersUiState.value.currentAddEditOrderState.value = ResultState.Success(ProductOrderCreateRequest())
        }
        else {
            fetchOrderDetails(orderId)
        }
    }

    private fun createProductVariantListFromOrderDetails(orderDetails: ProductOrderDetails?): List<ProductVariantOrderItem> {
        return orderDetails?.productOrderItems?.map { item ->
            ProductVariantOrderItem(
                productVariant = item.productVariant,
                quantity = item.amount,
                totalPrice = item.unitPrice * BigDecimal(item.amount)
            )
        } ?: emptyList()
    }

    private fun fetchOrderDetails(orderId: UUID) {
        performRepositoryAction(
            binding = _ordersUiState.value.currentAddEditOrderState,
            failureMessage = "Could not fetch product details, try again",
            action = {
                val orderDetails = orderRepository.getProductOrderDetails(orderId)
                orderDetails?.let {
                    val productOrderRequest = ProductOrderCreateRequest(orderDetails)
                    val productVariantList = createProductVariantListFromOrderDetails(orderDetails)
                    val orderTotal = calculateTotalPrice(productVariantList)
                    _ordersUiState.update { it.copy(
                        currentAddEditOrderDetails = orderDetails,
                        currentProductVariantDetailsList = productVariantList,
                        isCurrentOrderNew = false,
                        currentOrderTotalPrice = orderTotal
                    ) }
                    productOrderRequest
                }
            },
            onSuccess = {
                it.customerId?.let { customerId ->
                    getCustomerDetails(customerId)
                }
            }
        )
    }

    private fun getCustomerDetails(customerId: UUID) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not fetch customer details, try again",
            action = {
                val details = customerRepository.getCustomerDetails(customerId)
                if (details != null) {
                    _ordersUiState.update { it.copy(currentCustomerDetails = details) }
                }
            }
        )
    }

    fun updateSearchQuery(query: String) {
        _ordersUiState.update { currState ->
            currState.copy(
                searchQuery = query,
            )
        }
        applyFilters()
    }

    private fun applyFilters() {
        _ordersUiState.update { currState ->
            currState.copy(
                filteredSortedOrdersList = if (currState.showOrdersList) {
                    orderFilter.filterOrders(
                        templates = currState.ordersList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedOrdersList,
            )
        }
    }

    fun clearPriceFilter(isFrom: Boolean) {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    priceFrom = if (isFrom) null else currState.filterParams.priceFrom,
                    priceTo = if (!isFrom) null else currState.filterParams.priceTo
                )
            )
        }
        applyFilters()
    }

    fun clearTimeOfSendingFilter(isFrom: Boolean) {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    timeOfSendingFrom = if (isFrom) null else currState.filterParams.timeOfSendingFrom,
                    timeOfSendingTo = if (!isFrom) null else currState.filterParams.timeOfSendingTo
                )
            )
        }
        applyFilters()
    }

    fun clearTimeOfCreationFilter(isFrom: Boolean) {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    timeOfCreationFrom = if (isFrom) null else currState.filterParams.timeOfCreationFrom,
                    timeOfCreationTo = if (!isFrom) null else currState.filterParams.timeOfCreationTo
                )
            )
        }
        applyFilters()
    }

    fun clearStatusFilter() {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    status = null
                )
            )
        }
        applyFilters()
    }

    fun cancelFilterParams() {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = OrderFilterParams()
            )
        }
        applyFilters()
    }

    fun saveFilterParams(filterParams: OrderFilterParams) {
        _ordersUiState.update { currState ->
            currState.copy(
                filterParams = filterParams
            )
        }
        applyFilters()
    }

    fun fetchOrdersList(skipLoading: Boolean = false, onSuccessCallback: (List<ProductOrderSummary>) -> Unit) {
        performRepositoryAction(
            binding = _ordersUiState.value.ordersListFetched,
            failureMessage = "Could not fetch products, try again",
            skipLoading = skipLoading,
            action = { orderRepository.getAllProductOrders() },
            onSuccess = { ordersList ->
                _ordersUiState.update { it.copy(ordersList = ordersList) }
                calculateOrderTotals()
                applyFilters()
                onSuccessCallback(ordersList)
            }
        )
    }

    fun fetchOrdersListOnScreenLoad() {
        if (_ordersUiState.value.ordersListFetched.value is ResultState.Idle) {
            fetchOrdersList{}
        }
    }

    private fun calculateOrderTotals() {
        var totalAmount = 0
        var totalAmountNew = 0
        var totalPrice = BigDecimal.ZERO

        _ordersUiState.value.ordersList.forEach { order ->
            totalAmount += order.sampleProductOrderItem.amount
            totalPrice += order.totalDue
            if (order.status == Status.NEW) {
                totalAmountNew += order.sampleProductOrderItem.amount
            }
        }

        _ordersUiState.update { it.copy(
            amountOfItems = totalAmount,
            itemsSumPrice = totalPrice,
            amountOfItemsNewStatus = totalAmountNew,
        ) }
    }

    fun addProductVariantToList(productVariantList: List<ProductVariantOrderItem>, productVariantOrderItem: ProductVariantOrderItem): List<ProductVariantOrderItem> {
        val updatedProductVariantList = productVariantList.toMutableList()
        if (updatedProductVariantList.none { it.productVariant.productVariantId == productVariantOrderItem.productVariant.productVariantId }) {
            updatedProductVariantList.add(productVariantOrderItem)
        }
        return updatedProductVariantList
    }

    fun removeProductVariantFromList(productVariantList: List<ProductVariantOrderItem>, productVariantOrderItem: ProductVariantOrderItem): List<ProductVariantOrderItem> {
        val updatedProductVariantList = productVariantList.toMutableList()
        updatedProductVariantList.remove(productVariantOrderItem)
        return updatedProductVariantList
    }

    fun loadProductVariantImage(productVariantId: UUID) {
        viewModelScope.launch {
            _ordersUiState.update { currentState ->
                currentState.copy(
                    productVariantsImagesMap = currentState.productVariantsImagesMap + (productVariantId to null)
                )
            }

            val image = productRepository.getProductVariantPicture(productVariantId)
            _ordersUiState.update { currentState ->
                currentState.copy(
                    productVariantsImagesMap = currentState.productVariantsImagesMap + (productVariantId to image)
                )
            }
        }
    }

    fun loadCurrentOrderProductVariantImage(productVariantId: UUID) {
        viewModelScope.launch {
            _ordersUiState.update { currentState ->
                currentState.copy(
                    currentOrderProductVariantsImagesMap = currentState.currentOrderProductVariantsImagesMap + (productVariantId to null)
                )
            }

            val image = productRepository.getProductVariantPicture(productVariantId)
            _ordersUiState.update { currentState ->
                currentState.copy(
                    currentOrderProductVariantsImagesMap = currentState.currentOrderProductVariantsImagesMap + (productVariantId to image)
                )
            }
        }
    }

    fun calculateTotalPrice(productVariantList: List<ProductVariantOrderItem>): BigDecimal {
        return productVariantList.fold(BigDecimal.ZERO) { acc, item ->
            acc + item.totalPrice
        }
    }

    fun createHomeCardList(orderUIState: OrderUIState): List<HomeCardItem> {
        return listOf(
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = getStringResource(R.string.home_card_revenues),
                cardTitleIcon = Icons.Filled.Addchart,
                valueDescription = getStringResource(R.string.home_card_revenues_total),
                value = orderUIState.itemsSumPrice.toString()
            ),
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = getStringResource(R.string.home_card_sells),
                cardTitleIcon = Icons.Filled.BarChart,
                valueDescription = getStringResource(R.string.home_card_sells_total),
                value = orderUIState.amountOfItems.toString()
            ),
            HomeCardItem(
                backgroundImageResource = 0,
                cardTitle = getStringResource(R.string.home_card_new_orders),
                cardTitleIcon = Icons.Filled.StackedLineChart,
                valueDescription = getStringResource(R.string.home_card_new_orders_total),
                value = orderUIState.amountOfItemsNewStatus.toString()
            )
        )
    }

    fun getStringResource(resource: Int): String {
        return context.getString(resource)
    }

    enum class SortingType {
        NONE,
        ASCENDING_PRICE, DESCENDING_PRICE,
        ASCENDING_TIME_OF_SENDING, DESCENDING_TIME_OF_SENDING,
        ASCENDING_TIME_OF_CREATION, DESCENDING_TIME_OF_CREATION
    }

    data class OrderFilterParams(
        val sortingType: SortingType = SortingType.NONE,
        val status: Status? = null,
        val priceFrom: BigDecimal? = null,
        val priceTo: BigDecimal? = null,
        val timeOfSendingFrom: LocalDateTime? = null,
        val timeOfSendingTo: LocalDateTime? = null,
        val timeOfCreationFrom: LocalDateTime? = null,
        val timeOfCreationTo: LocalDateTime? = null
    )

    data class HomeCardItem(
        val backgroundImageResource: Int,
        val cardTitle: String,
        val cardTitleIcon: ImageVector,
        val valueDescription: String,
        val value: String
    )

    data class ProductVariantOrderItem(
        val productVariant: ProductVariantDetails,
        val quantity: Int,
        val totalPrice: BigDecimal
    )
}