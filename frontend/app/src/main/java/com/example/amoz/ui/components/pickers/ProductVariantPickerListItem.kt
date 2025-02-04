package com.example.amoz.ui.components.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.pickers.ProductVariantPicker
import com.example.amoz.ui.components.bottom_sheets.QuantityBottomSheet
import com.example.amoz.view_models.OrdersViewModel
import java.math.BigDecimal

@Composable
fun ProductVariantPickerWithListItem(
    modifier: Modifier = Modifier,
    onProductVariantChange: (OrdersViewModel.ProductVariantOrderItem) -> Unit,
    onSaveState: () -> Unit,
    navController: NavController,
) {
    val productVariantPicker = ProductVariantPicker(navController)
    val selectedProductVariant = productVariantPicker.getPickedProductVariant()
    var showBottomSheet by remember { mutableStateOf(false) }

    var currentProductVariant by remember { mutableStateOf<ProductVariantDetails?>(null) }

    LaunchedEffect(selectedProductVariant) {
        if (selectedProductVariant != null && !showBottomSheet) {
            productVariantPicker.removePickedProductVariant()
            showBottomSheet = true
            currentProductVariant = selectedProductVariant
        }
    }

    ProductVariantPickerText(
        modifier = modifier,
        onClick = {
            onSaveState()
            productVariantPicker.navigateToProductScreen()
        }
    )

    if (showBottomSheet) {
        currentProductVariant?.let { productVariant ->
            QuantityBottomSheet(
                stock = productVariant.stock.amountAvailable,
                onDismiss = {
                    showBottomSheet = false
                            },
                onConfirm = { quantity ->
                    showBottomSheet = false
                    onProductVariantChange(
                        OrdersViewModel.ProductVariantOrderItem(
                            productVariant = productVariant,
                            quantity = quantity,
                            totalPrice = productVariant.variantPrice * BigDecimal(quantity)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun ProductVariantPickerText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 10.dp)
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.add_new_product),
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_new_product),
        )
    }
}