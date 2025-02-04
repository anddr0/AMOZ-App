import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.models.ProductOrderSummary
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.view_models.OrdersViewModel
import java.util.UUID

@Composable
fun OrderListItem(
    order: ProductOrderSummary,
    onOrderEdit: ((UUID) -> Unit)? = null,
    currency: String,
    ordersViewModel: OrdersViewModel
) {
    val ordersUiState by ordersViewModel.ordersUiState.collectAsState()
    val productVariantId = order.sampleProductOrderItem.productVariant.productVariantId

    LaunchedEffect(productVariantId) {
        ordersViewModel.loadProductVariantImage(productVariantId)
    }

    val image = ordersUiState.productVariantsImagesMap[productVariantId]

    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = { onOrderEdit?.invoke(order.productOrderId) },
                enabled = onOrderEdit != null
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
                text = order.sampleProductOrderItem.productVariant?.variantName ?: "Empty order",
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
            Text(
                text = "${order.totalDue} $currency"
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    )
}