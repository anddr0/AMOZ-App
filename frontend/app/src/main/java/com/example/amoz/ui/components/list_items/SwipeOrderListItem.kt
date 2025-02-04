package com.example.amoz.ui.components.list_items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.bottom_sheets.ConfirmDeleteItemBottomSheet
import com.example.amoz.ui.components.dissmiss_backgrounds.OrderDismissBackground
import com.example.amoz.view_models.OrdersViewModel
import java.util.UUID

@Composable
fun SwipeOrderListItem(
    order: ProductOrderSummary,
    onOrderRemove: (UUID) -> Unit,
    onOrderEdit: (UUID) -> Unit,
    currency: String,
    ordersViewModel: OrdersViewModel
) {
    val ordersUiState by ordersViewModel.ordersUiState.collectAsState()
    val productVariantId = order.sampleProductOrderItem.productVariant.productVariantId

    var showDeleteBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(productVariantId) {
        ordersViewModel.loadProductVariantImage(productVariantId)
    }

    val image = ordersUiState.productVariantsImagesMap[productVariantId]

    var currentFraction by remember { mutableFloatStateOf(0f) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            when (newValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (currentFraction >= 0.45f && currentFraction < 1.0f) {
                        showDeleteBottomSheet = true
                        return@rememberSwipeToDismissBoxState false
                    }
                    return@rememberSwipeToDismissBoxState false
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        backgroundContent = {
            currentFraction = swipeState.progress
            OrderDismissBackground(swipeState)
        }
    ) {
        ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = { onOrderEdit(order.productOrderId) }),
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
                    text = order.sampleProductOrderItem.productVariant.variantName ?: "Empty order",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                order.status.getName().let {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = it,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Icon(
                            modifier = Modifier.fillMaxSize(0.05f),
                            imageVector = Icons.Default.Circle,
                            contentDescription = null,
                            tint = order.status.color
                        )
                    }
                }
            },
            trailingContent = {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
                ) {
                    Text(
                        text = "${order.totalDue} $currency"
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowCircleRight,
                        contentDescription = null
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
        )
    }

    if (showDeleteBottomSheet) {
        ConfirmDeleteItemBottomSheet(
            onDismissRequest = { showDeleteBottomSheet = false },
            onDeleteConfirm = { onOrderRemove(order.productOrderId) },
            itemNameToDelete = order.sampleProductOrderItem.productVariant.variantName?: ""
        )
    }
}
