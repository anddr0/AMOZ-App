package com.example.amoz.ui.screens.bottom_screens.products.products_list.list_items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.dissmiss_backgrounds.DismissBackground
import com.example.amoz.ui.components.ResultStateView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ProductVariantListItem(
    productVariant: ProductVariantSummary,
    productVariantBitmapImageState: MutableStateFlow<ResultState<ImageBitmap?>>?,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    currency: String,
    positionalThreshold: Float = .45f
) {
    val imageBitmapFromState = (productVariantBitmapImageState?.collectAsState()?.value as? ResultState.Success)?.data

    var currentFraction by remember { mutableStateOf(0f) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            when (newValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (currentFraction >= positionalThreshold && currentFraction < 1.0f) {
                        onDelete()
                        return@rememberSwipeToDismissBoxState false
                    }
                    return@rememberSwipeToDismissBoxState false
                }
                else -> false
            }
        },
        positionalThreshold = { it * positionalThreshold }
    )

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromEndToStart = false,
        backgroundContent = {
            currentFraction = swipeState.progress
            DismissBackground(swipeState)
        }
    ) {
        ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    onClick = onClick,
                ),
            leadingContent =
            {
                ImageWithIcon(
                    image = imageBitmapFromState,
                    placeholder = ImagePlaceholder.PRODUCT,
                    contentDescription = "Product Image",
                    size = 40.dp,
                    isEditing = false
                )
            }
            ,
            headlineContent = {
                Text(
                    text = productVariant.variantName ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null
                    )
                    Text(text = "Barcode: ${productVariant.code}")
                }
            },
            trailingContent = {
                Text(
                    text = "${productVariant.variantPrice} $currency",
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
        )
    }
}