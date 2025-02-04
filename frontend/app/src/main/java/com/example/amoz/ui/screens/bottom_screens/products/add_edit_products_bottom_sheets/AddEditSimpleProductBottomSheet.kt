package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.requests.StockCreateRequest
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.pickers.CategoryPickerListItem
import com.example.amoz.ui.screens.bottom_screens.products.attributes.ProductAttributes
import com.example.amoz.view_models.ProductsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSimpleProductBottomSheet(
    viewModel: ProductsViewModel,
    productCreateRequest: ProductCreateRequest,
    productVariantCreateRequest: ProductVariantCreateRequest,
    onComplete: (ProductCreateRequest, ProductVariantCreateRequest, Uri?) -> Unit,
    onSaveState: (Pair<ProductCreateRequest, ProductVariantCreateRequest>) -> Unit,
    onDismissRequest: () -> Unit,
    navController: NavController,
) {
    var productState by remember { mutableStateOf(productCreateRequest) }
    var productVariantState by remember { mutableStateOf(productVariantCreateRequest) }

    var categoryTreeState by remember { mutableStateOf<CategoryTree?>(null) }

    var validationMessage by remember { mutableStateOf<String?>(null) }

    var productVariantImageUri by remember { mutableStateOf<Uri?>(null) }

    val scope = rememberCoroutineScope()
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            }
        )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
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
                text = stringResource(R.string.products_add_product_variant),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            // -------------------- Product image --------------------
            ImageWithIcon(
                image = productVariantImageUri?.toString(),
                shape = RoundedCornerShape(10.dp),
                placeholder = ImagePlaceholder.PRODUCT,
                onImagePicked = { imageUri ->
                    productVariantImageUri = imageUri
                }
            )

            // -------------------- Product basic info --------------------
            ProductNameDescriptionPrice(
                productName = productState.name,
                showProductDescription = true,
                productDescription = productState.description,
                productPrice = productState.price,
                onNameChange = {
                    productState = productState.copy(name = it)
                    productVariantState = productVariantState.copy(variantName = it)
                },
                onDescriptionChange = {
                    productState = productState.copy(description = it)
                },
                onPriceChange = {
                    productState = productState.copy(price = it)
                    productVariantState = productVariantState.copy(variantPrice = it)
                }
            )

            // -------------------- Product barcode --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = productVariantState.productVariantCode?.toString() ?: "",
                onValueChange = { newBarcode ->
                    productVariantState = productVariantState.copy(
                        productVariantCode = newBarcode.toIntOrNull()
                    )
                },
                label = { Text(text = stringResource(R.string.product_barcode)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                maxLines = 1,
                singleLine = true
            )

            // -------------------- Product brand --------------------
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(stringResource(R.string.product_brand)) },
                value = productState.brand ?: "",
                onValueChange = {
                    productState = productState.copy(brand = it.takeIf { it.isNotBlank() })
                },
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
                maxLines = 1,
                singleLine = true
            )

            //  -------------------- Product's category --------------------
            CategoryPickerListItem(
                category = categoryTreeState,
                navController = navController,
                leavesOnly = true,
                onSaveState = { onSaveState(Pair(productState, productVariantState)) },
                onCategoryChange = {
                    categoryTreeState = it
                    productState = productState.copy(categoryId = categoryTreeState?.categoryId)
                },
                getCategoryId = { it?.categoryId },
                getCategoryName = { it?.name }
            )

            // -------------------- Stock --------------------
            ProductVariantStock(
                stockCreateRequest = productVariantState.stock ?: StockCreateRequest()
            ) {
                productVariantState = productVariantState.copy(stock = it)
            }

            // -------------------- Weight --------------------
            ProductVariantWeight(productVariantState.weight) {
                productVariantState = productVariantState.copy(weight = it)
            }

            // -------------------- Dimensions --------------------
            ProductVariantDimensions(productVariantState.dimensions) {
                productVariantState = productVariantState.copy(dimensions = it)
            }

            // -------------------- Attributes --------------------
            ProductAttributes(
                viewModel = viewModel,
                productAttributes = productState.productAttributes,
                onAttributesChange = {
                    productState = productState.copy(productAttributes = it)
                    productVariantState = productVariantState.copy(variantAttributes = it)
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
                    val productViolation = productState.validate()
                    val productVariantViolation = productVariantState.validate()


                    if ((productVariantViolation != null && productVariantViolation.contains("Product variant of product"))) {
                        validationMessage += "\n" + productVariantViolation
                    }
                    if (productViolation != null) {
                        validationMessage = "\n" + productViolation
                    }
                    else {
                        onComplete(productState, productVariantState, productVariantImageUri)
                        onDismissRequest()
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