package com.example.amoz.ui.screens.bottom_screens.orders

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.app.AppPreferences
import com.example.amoz.ui.components.ErrorText
import com.example.amoz.ui.components.dropdown_menus.StatusDropdownMenu
import com.example.amoz.ui.components.bottom_sheets.AddressBottomSheet
import com.example.amoz.ui.components.HorizontalDividerWithTextBefore
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.PrimaryOutlinedButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.list_items.CurrentOrderCustomerListItem
import com.example.amoz.ui.components.pickers.ProductVariantPickerWithListItem
import com.example.amoz.ui.components.text_fields.AddressTextField
import com.example.amoz.ui.components.text_fields.DateTextField
import com.example.amoz.ui.components.list_items.OrdersProductListItem
import com.example.amoz.ui.components.pickers.CustomerPickerListItem
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.OrdersViewModel
import java.math.BigDecimal
import java.time.LocalDateTime

@Composable
fun OrdersAddEditScreen (
    navController: NavController,
    paddingValues: PaddingValues,
    ordersViewModel: OrdersViewModel
) {
    val ordersUiState by ordersViewModel.ordersUiState.collectAsState()

    val currContext = LocalContext.current
    val appPreferences = remember { AppPreferences(currContext) }
    val currency by appPreferences.currency.collectAsState(initial = "USD")

    val isNewOrder = ordersUiState.isCurrentOrderNew
    var totalPrice = ordersUiState.currentOrderTotalPrice
    var currentCustomerDetails = ordersUiState.currentCustomerDetails
    var productVariantList = ordersUiState.currentProductVariantDetailsList

    var validationErrorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        ResultStateView(ordersUiState.currentAddEditOrderState) { request ->
            var orderRequest by remember { mutableStateOf(request) }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                // ---------------------------- Status Dropdown ----------------------------
                item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_status)) }

                    item {
                        StatusDropdownMenu(
                            selectedStatus = orderRequest.status,
                            onStatusChange = { orderRequest = orderRequest.copy(status = it!!) },
                            noStatusSelectedPossible = false,
                        )
                    }

                // ---------------------------- Products List ----------------------------

                item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_products)) }

                items(
                    productVariantList,
                    key = { it.productVariant.productVariantId }
                ) { productVariantItem ->
                    OrdersProductListItem(
                        product = productVariantItem,
                        onProductRemove = { product ->
                            productVariantList = ordersViewModel.removeProductVariantFromList(
                                productVariantList,
                                product
                            )
                            totalPrice = ordersViewModel.calculateTotalPrice(productVariantList)
                            ordersViewModel.saveCurrentAddEditOrderState(
                                orderRequest,
                                productVariantList,
                                currentCustomerDetails,
                                totalPrice,
                            )
                        },
                        ordersViewModel = ordersViewModel,
                        currency = currency,
                        isNewOrder = isNewOrder
                    )
                }

                item {
                    if (isNewOrder) {
                        ProductVariantPickerWithListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp)),
                            onProductVariantChange = { productVariantOrderItem ->
                                productVariantList = ordersViewModel.addProductVariantToList(
                                    productVariantList,
                                    productVariantOrderItem,
                                )
                                totalPrice = ordersViewModel.calculateTotalPrice(productVariantList)
                                ordersViewModel.saveCurrentAddEditOrderState(
                                    orderRequest,
                                    productVariantList,
                                    currentCustomerDetails,
                                    totalPrice,
                                )
                            },
                            onSaveState = {
                                ordersViewModel.saveCurrentAddEditOrderState(
                                    orderRequest,
                                    productVariantList,
                                    currentCustomerDetails,
                                    totalPrice,
                                )
                            },
                            navController = navController,
                        )
                    }
                }

                // ---------------------------- Customer ListItem ----------------------------
                item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_customer)) }

                item {
                    if (currentCustomerDetails != null) {
                        CurrentOrderCustomerListItem(
                            currentCustomer = currentCustomerDetails!!,
                            onRemoveCustomer = {
                                orderRequest = orderRequest.copy(customerId = null)
                                currentCustomerDetails = null

                                ordersViewModel.saveCurrentAddEditOrderState(
                                    orderRequest,
                                    productVariantList,
                                    currentCustomerDetails,
                                    totalPrice,
                                )
                            },
                            onSendInvoice = {
                                ordersViewModel.generateProductOrderInvoice(ordersUiState.currentAddEditOrderDetails!!.productOrderId)
                            },
                            isNewOrder = isNewOrder,
                        )
                    } else {
                        CustomerPickerListItem(
                        onCustomerChange = {
                            orderRequest = orderRequest.copy(customerId = it.customerId)
                            currentCustomerDetails = it
                            ordersViewModel.saveCurrentAddEditOrderState(
                                orderRequest,
                                productVariantList,
                                currentCustomerDetails,
                                totalPrice,
                            )
                                           },
                        onSaveState = {
                            ordersViewModel.saveCurrentAddEditOrderState(
                                orderRequest,
                                productVariantList,
                                currentCustomerDetails,
                                totalPrice,
                            )
                        },
                        navController = navController
                    )}
                }

                // ---------------------------- Shipping Number, Address, Date ----------------------------

                item {  HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_shipping)) }

                item {
                    AddressTextField (
                        modifier = Modifier.height(R.dimen.text_field_height.dp),
                        trailingIcon = Icons.AutoMirrored.Outlined.ArrowForward,
                        address = orderRequest.address,
                        onClick = {
                            ordersViewModel.changeAddressBottomSheetStatus(true)
                        }
                    )
                }

                item {
                    OutlinedTextField(
                        value = orderRequest.trackingNumber ?: "",
                        onValueChange = {
                            if (it.length <= 10) orderRequest =
                                orderRequest.copy(trackingNumber = it)
                        },
                        label = { Text(stringResource(R.string.orders_tracking_number)) },
                        modifier = Modifier
                            .height(R.dimen.text_field_height.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }

                item {
                    DateTextField(
                        label = stringResource(R.string.orders_shipping_date),
                        date = orderRequest.timeOfSending,
                        onDateChange = {
                            orderRequest = orderRequest.copy(timeOfSending = it as? LocalDateTime)
                        },
                        trailingIcon = Icons.Default.DateRange,
                        showTime = true,
                        formatAsDateOnly = false,
                    )
                }

                item { HorizontalDividerWithTextBefore(text = stringResource(R.string.orders_total_price)) }

                item {
                    Row {
                        Text(
                            text = "${stringResource(R.string.orders_total_price)}:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Text(
                            text = "$totalPrice $currency",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                item {
                    ErrorText(errorMessage = validationErrorMessage)
                }

                // checks whether its a new order or an existing one and performs the appropriate action
                item {
                    PrimaryFilledButton(
                        text = if (isNewOrder) { stringResource(R.string.orders_create_an_order) }
                        else { stringResource(R.string.orders_update_an_order) },
                        onClick = {
                            try {
                                ordersViewModel.chooseProductOrderOperation(
                                    orderRequest,
                                    productVariantList
                                )
                            } catch (e: IllegalArgumentException) {
                                validationErrorMessage = e.message
                            } finally {
                                navController.popBackStack()
                            }
                        }
                    )
                }

                item {
                    PrimaryOutlinedButton(
                        text = stringResource(R.string.cancel),
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                }

                if (ordersUiState.isAddressBottomSheetExpanded) {
                    item {
                        AddressBottomSheet(
                            bottomSheetTitle = stringResource(id = R.string.orders_change_shipment_address),
                            onDismissRequest = {
                                ordersViewModel.changeAddressBottomSheetStatus(false)
                            },
                            address = orderRequest.address ?: AddressCreateRequest(),
                            onDone = { request ->
                                ordersViewModel.updateAddress(request)
                                orderRequest = orderRequest.copy(address = request)
                            }
                        )
                    }
                }
            }
        }
    }
}