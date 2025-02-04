package com.example.amoz.ui.screens.bottom_screens.products.add_edit_products_bottom_sheets

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.requests.StockCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.components.CloseOutlinedButton
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.ui.components.pickers.ProductPickerWithListItem
import com.example.amoz.ui.screens.bottom_screens.products.attributes.ProductAttributes
import com.example.amoz.view_models.ProductsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductVariantBottomSheet(
    viewModel: ProductsViewModel,
    productVariantCreateRequest: ProductVariantCreateRequest,
    productVariantDetailsState: MutableStateFlow<ResultState<ProductVariantDetails?>>,
    productVariantImageState: MutableStateFlow<ResultState<ImageBitmap?>>,
    productMainVariantId: UUID?,
    onSaveProductVariant: (ProductVariantCreateRequest) -> Unit,
    onComplete: (UUID?, ProductVariantCreateRequest, Uri?, Boolean, (String?) -> Unit) -> Unit,
    onDismissRequest: () -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    var validationMessage by remember { mutableStateOf<String?>(null) }

    var hasProductId by remember { mutableStateOf(productVariantCreateRequest.productID != null) }

    var productVariantImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember(productVariantCreateRequest.productID) { mutableStateOf<ImageBitmap?>(null) }

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
        ResultStateView(state = productVariantDetailsState) { productVariantDetails ->
            var productVariantState by remember(productVariantDetails, productVariantCreateRequest) {
                mutableStateOf(
                    ProductVariantCreateRequest(
                        productVariant = productVariantDetails,
                        productID = productVariantCreateRequest.productID
                    )
                )
            }


            var isMainVariant by remember { mutableStateOf(
                productVariantDetails?.productVariantId == productMainVariantId
            )}

            LaunchedEffect(productVariantState) {
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
                    text = stringResource(R.string.products_add_product_variant),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                // -------------------- Product image --------------------
                ResultStateView(state = productVariantImageState) { imageBitmap = it }
                ImageWithIcon(
                    image = productVariantImageUri?.toString() ?: imageBitmap,
                    shape = RoundedCornerShape(10.dp),
                    placeholder = ImagePlaceholder.PRODUCT,
                    onImagePicked = { imageUri ->
                        productVariantImageUri = imageUri
                    }
                )


                // -------------------- Product basic info --------------------
                ProductNameDescriptionPrice(
                    productName = productVariantState.variantName,
                    productPrice = productVariantState.variantPrice,
                    onNameChange = {
                        productVariantState = productVariantState.copy(variantName = it)
                    },
                    onPriceChange = {
                        productVariantState = productVariantState.copy(variantPrice = it)
                    },
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

                // -------------------- Product variant of --------------------
                if (!hasProductId) {
                    ProductPickerWithListItem(
                        onProductChange = {
                            productVariantState = productVariantState.copy(productID = it.productId)
                            hasProductId = false
                        },
                        onSaveState = { onSaveProductVariant(productVariantState) },
                        navController = navController,
                    )
                }

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            SolidColor(MaterialTheme.colorScheme.outline),
                            RoundedCornerShape(10.dp)
                        ),
                    headlineContent = {
                        Text(stringResource(R.string.product_variant_set_as_main))
                    },
                    trailingContent = {
                        Switch(isMainVariant, { isMainVariant = !isMainVariant })
                    }
                )


                // -------------------- Stock --------------------
                ProductVariantStock(
                    stockCreateRequest = productVariantState.stock ?: StockCreateRequest()) {
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
                    productAttributes = productVariantState.variantAttributes,
                    onAttributesChange = {
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
                        onComplete(
                            productVariantDetails?.productVariantId,
                            productVariantState,
                            productVariantImageUri,
                            isMainVariant
                        ) {
                            if(it == null) { onDismissRequest() }
                            else { validationMessage = it }
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

