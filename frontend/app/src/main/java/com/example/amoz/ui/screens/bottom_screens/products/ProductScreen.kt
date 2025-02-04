package com.example.amoz.ui.screens.bottom_screens.products

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.ui.theme.AmozApplicationTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.pickers.SavedStateHandleKeys
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.productScreenBottomSheetMenu
import com.example.amoz.ui.components.bottom_sheets.ConfirmDeleteItemBottomSheet
import com.example.amoz.ui.components.filters.MoreFiltersBottomSheet
import com.example.amoz.view_models.ProductsViewModel.BottomSheetType
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditProductVariantBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets.AddEditSimpleProductBottomSheet
import com.example.amoz.ui.screens.bottom_screens.products.products_list.FilteredProductsList
import com.example.amoz.view_models.ProductsViewModel
import java.util.UUID

@Composable
fun ProductScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val productsUiState by productsViewModel.productUiState.collectAsState()
    val currency by productsViewModel.getCurrency().collectAsState(initial = "USD")
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle

    LaunchedEffect(true) {
        productsViewModel.fetchProductsListOnScreenLoad()
    }

    fun onMenuItemClick(
        navItemType: NavItemType
    ) {
        when (navItemType) {
            NavItemType.AddProduct -> {
                productsViewModel.updateCurrentAddEditProduct(null)
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_PRODUCT, true)
            }
            NavItemType.AddProductVariant -> {
                productsViewModel.updateCurrentAddEditProductVariant(null, null)
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, true)
            }
            NavItemType.AddSimpleProduct -> {
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_SIMPLE, true)
            }
            else -> {
                savedStateHandle?.set(SavedStateHandleKeys.CATEGORY_PICKER_MODE, false)
                navController.navigate(
                    productScreenBottomSheetMenu[navItemType]!!.screenRoute
                )
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = 5.dp),
        color = MaterialTheme.colorScheme.background
    ) {

        FilteredProductsList(
            navController = navController,
            currency = currency!!
        )

        // -------------------- Menu FAB --------------------
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    productsViewModel.expandBottomSheet(BottomSheetType.MENU, true)
                },
                modifier = Modifier
                    .padding(16.dp),
                icon = { Icon(Icons.Filled.Menu, null) },
                text = { Text(text = "Menu") }
            )
        }

    }
    // -------------------- Menu Bottom Sheet --------------------
    if (productsUiState.menuBottomSheetExpanded) {
        MenuBottomSheet(
            onDismissRequest = {
                productsViewModel.expandBottomSheet(BottomSheetType.MENU, false)
            },
            onClick = { onMenuItemClick(it) },
        )
    }

    // -------------------- More product filters Bottom Sheet --------------------
    if (productsUiState.moreFiltersBottomSheetExpanded) {
        MoreFiltersBottomSheet (
            onDismissRequest = {
                productsViewModel.saveFilterParamsInEdit(productsUiState.filterParams)
                productsViewModel.expandBottomSheet(BottomSheetType.MORE_FILTERS, false)
            },
            onApplyFilters = productsViewModel::updateFilterParams,
            onSaveFilterParams = productsViewModel::saveFilterParamsInEdit,
            onCancelFilters = productsViewModel::cancelFilters,
            filterParams = productsUiState.filterParamsInEdit,
            navController = navController
        )
    }

    // -------------------- Confirm product delete --------------------
    if (productsUiState.deleteProductBottomSheetExpanded) {
         productsUiState.currentProductToDelete?.let {
            ConfirmDeleteItemBottomSheet(
                onDismissRequest = {
                    productsViewModel.updateCurrentProductToDelete(null)
                    productsViewModel.expandBottomSheet(BottomSheetType.DELETE_PRODUCT, false)
                },
                onDeleteConfirm = {
                    productsViewModel.deleteProduct(it.productId)
                },
                itemNameToDelete = it.name
            )
        }
        productsUiState.currentProductVariantToDelete?.let { productVariantToDelete ->
            ConfirmDeleteItemBottomSheet(
                onDismissRequest = {
                    productsViewModel.updateCurrentProductToDelete(null)
                    productsViewModel.expandBottomSheet(BottomSheetType.DELETE_PRODUCT, false)
                },
                onDeleteConfirm = {
                    productsViewModel.deleteProductVariant(productVariantToDelete.productVariantId)
                },
                itemNameToDelete = productVariantToDelete.variantName ?: ""
            )
        }
    }

    // -------------------- Add/Edit Product --------------------
    if (productsUiState.addEditProductBottomSheetExpanded) {
        AddEditProductBottomSheet(
            viewModel = productsViewModel,
            productDetailsState = productsUiState.productDetailsState,
            savedProductState = productsUiState.productCreateRequest,
            onSaveProduct = productsViewModel::saveCurrentProductCreateRequest,
            onComplete = { productId, productState, onErrorCallback ->
                productId?.let {
                    try {
                        productsViewModel.updateProduct(it, productState)
                        onErrorCallback(null)
                    }
                    catch(e: IllegalArgumentException) {onErrorCallback(e.message)}
                }
                ?: run {
                    try{
                        productsViewModel.createProduct(productState)
                        onErrorCallback(null)
                    }
                    catch(e: IllegalArgumentException) { onErrorCallback(e.message) }
                }
            },
            onDismissRequest = {
                productsViewModel.updateCurrentAddEditProduct(null)
                productsViewModel.expandBottomSheet(
                    BottomSheetType.ADD_EDIT_PRODUCT,
                    false
                )
            },
            navController = navController,
        )
    }

    // -------------------- Add/Edit Product Variant --------------------
    if (productsUiState.addEditProductVariantBottomSheetExpanded) {
        val productMainVariantId =  productsUiState.filteredByProduct?.mainProductVariant?.productVariantId

        AddEditProductVariantBottomSheet(
            viewModel = productsViewModel,
            productVariantCreateRequest = productsUiState.productVariantCreateRequest,
            productVariantDetailsState = productsUiState.productVariantDetailsState,
            productVariantImageState = productsUiState.productVariantImageState,
            productMainVariantId = productMainVariantId,

            onComplete = { productVariantId, productVariantCreateRequest, imageUri, isMainVariant, onErrorCallback ->
                productVariantId?.let {
                    try {
                        productsViewModel.updateProductVariant(
                            productVariantId,
                            productVariantCreateRequest
                        ) {
                            imageUri?.let {
                                productsViewModel.uploadProductVariantImage(
                                    productVariantId,
                                    imageUri
                                )
                            }
                            productsViewModel.setProductMainVariant(
                                productVariantCreateRequest.productID!!,
                                productVariantId
                            )
                        }
                        onErrorCallback(null)
                    }
                    catch (e: IllegalArgumentException) { onErrorCallback(e.message) }
                }
                ?: run {
                    try {
                        productsViewModel.createProductVariant(productVariantCreateRequest) { productVariantDetails ->
                            imageUri?.let {
                                productsViewModel.uploadProductVariantImage(
                                    productVariantDetails.productVariantId, imageUri
                                )
                            }
                            productsViewModel.setProductMainVariant(
                                productVariantCreateRequest.productID!!,
                                productVariantDetails.productVariantId
                            )
                        }
                        onErrorCallback(null)
                    }
                    catch (e: IllegalArgumentException) { onErrorCallback(e.message) }
                }

            },
            onSaveProductVariant = productsViewModel::saveCurrentAddEditProductVariant,
            onDismissRequest = {
                productsViewModel.updateProductVariantImageStateIdle()
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_VARIANT, false)
            },
            navController = navController
        )
    }

    // -------------------- Add/Edit Simple Product --------------------
    if (productsUiState.addEditSimpleProductBottomSheetExpanded) {
        val productState = productsUiState.currentAddEditSimpleProduct.first
        val productVariantState = productsUiState.currentAddEditSimpleProduct.second
        AddEditSimpleProductBottomSheet(
            viewModel = productsViewModel,
            navController = navController,
            productCreateRequest = productState,
            productVariantCreateRequest = productVariantState,
            onComplete = { product, productVariant, imageUri ->
                productsViewModel.createSimpleProduct(product, productVariant) { _, productVariantDetails ->
                    imageUri?.let { productsViewModel.uploadProductVariantImage(
                        productVariantDetails.productVariantId, it
                        )
                    }
                }
            },
            onSaveState = productsViewModel::saveSimpleProduct,
            onDismissRequest = {
                productsViewModel.updateCurrentAddEditProduct(null)
                productsViewModel.expandBottomSheet(BottomSheetType.ADD_EDIT_SIMPLE, false)
            }
        )
    }
}




