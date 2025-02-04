package com.example.amoz.ui.components.list_items

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.view_models.OrdersViewModel
import com.example.amoz.view_models.OrdersViewModel.ProductVariantOrderItem

@Composable
fun OrdersProductListItem(
    product: ProductVariantOrderItem,
    onProductRemove: (ProductVariantOrderItem) -> Unit,
    currency: String,
    ordersViewModel: OrdersViewModel,
    isNewOrder: Boolean = true,
    positionalThreshold: Float = .45f
) {
    val ordersUiState by ordersViewModel.ordersUiState.collectAsState()
    val productVariantId = product.productVariant.productVariantId

    var showDialog by remember { mutableStateOf(false) }

    val image = ordersUiState.currentOrderProductVariantsImagesMap[productVariantId]

    LaunchedEffect (productVariantId) { ordersViewModel.loadCurrentOrderProductVariantImage(productVariantId) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.remove_product_from_order_title)) },
            text = { Text(stringResource(R.string.remove_product_from_order_text)) },
            confirmButton = {
                Button(
                    onClick = {
                        onProductRemove(product)
                        showDialog = false
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(
                onClick = {
                    if (isNewOrder) {
                        showDialog = true
                    }
                }
            ),
        leadingContent = {
            ImageWithIcon(
                image = image,
                placeholder = ImagePlaceholder.PRODUCT,
                contentDescription = "Product Image",
                size = 40.dp,
                isEditing = false
            )
        },
        headlineContent = {
            Text(
                text = "${product.productVariant.variantName} x${product.quantity}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Text(
                text = "${product.totalPrice} $currency"
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
    )
}
