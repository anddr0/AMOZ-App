package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CategoryTree
import com.example.amoz.models.ProductDetails
import com.example.amoz.ui.components.pickers.CategoryPickerListItem
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.text_fields.AutoCompleteTextField
import com.example.amoz.ui.screens.bottom_screens.products.attributes.ProductAttributes
import com.example.amoz.view_models.ProductsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductBottomSheet(
    viewModel: ProductsViewModel,
    productDetailsState: MutableStateFlow<ResultState<ProductDetails?>>,
    savedProductState: ProductCreateRequest?,
    onSaveProduct: (ProductCreateRequest) -> Unit,
    onComplete: (UUID?, ProductCreateRequest, (String?) -> Unit) -> Unit,
    onDismissRequest: () -> Unit,
    navController: NavController
) {
    var validationMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            }
        )
    fun hideBottomSheet() {
        scope.launch {
            sheetState.hide()
            onDismissRequest()
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        ResultStateView(productDetailsState) { productDetails ->
            var productState by remember(savedProductState) {
                mutableStateOf(
                    savedProductState ?: ProductCreateRequest(productDetails)
                )
            }

            var categoryTreeState by remember { mutableStateOf(
                productDetails?.category?.let {CategoryTree(it) }
            ) }

            LaunchedEffect(productState) {
                validationMessage = null
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // -------------------- Bottom sheet title --------------------
                Text(
                    text = stringResource(R.string.products_add_product),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(10.dp))

                // -------------------- Product basic info --------------------
                ProductNameDescriptionPrice(
                    productName = productState.name,
                    showProductDescription = true,
                    productDescription = productState.description,
                    productPrice = productState.price,
                    onNameChange = { productState = productState.copy(name = it) },
                    onDescriptionChange = { productState = productState.copy(description = it) },
                    onPriceChange = { productState = productState.copy(price = it) },
                )

                // -------------------- Product brand --------------------
                AutoCompleteTextField(
                    completionList = viewModel.productUiState.collectAsState().value.productsList.map { it.brand }.filterNotNull(),
                    value = productState.brand ?: "",
                    onValueChange = {
                        productState = productState.copy(brand = it.takeIf { it.isNotBlank() })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.product_brand)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Verified,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    maxLines = 1
                )
//             -------------------- Product's category --------------------
                CategoryPickerListItem(
                    category = categoryTreeState,
                    navController = navController,
                    leavesOnly = true,
                    onSaveState = { onSaveProduct(productState) },
                    onCategoryChange = {
                        categoryTreeState = it
                        productState = productState.copy(categoryId = categoryTreeState?.categoryId)
                    },
                    getCategoryId = { it?.categoryId },
                    getCategoryName = { it?.name }
                )


                // -------------------- Product attributes --------------------
                ProductAttributes(
                    viewModel = viewModel,
                    productAttributes = productState.productAttributes,
                    onAttributesChange = {
                        productState = productState.copy(productAttributes = it)
                    }
                )

                // -------------------- Complete adding --------------------
                Spacer(modifier = Modifier.height(15.dp))

                validationMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                PrimaryFilledButton(
                    onClick = {
                        onComplete(productDetails?.productId, productState) {
                            Log.d("ERROR asd", it.toString())
                            if (it != null) { validationMessage = it  }
                            else { onDismissRequest() }
                        }
                    },
                    text = stringResource(id = R.string.save),
                )
                // -------------------- Close bottom sheet --------------------
                CloseOutlinedButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onDismissRequest()
                        }
                    },
                    text = stringResource(R.string.close)
                )
            }
        }
    }
}





