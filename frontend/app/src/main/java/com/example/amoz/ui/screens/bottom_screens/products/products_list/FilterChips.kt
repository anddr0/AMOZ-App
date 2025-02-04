package com.example.amoz.ui.screens.bottom_screens.products.products_list

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.models.CategoryTree
import java.math.BigDecimal

@Composable
fun FilterChips(
    modifier: Modifier = Modifier,
    productTemplateChipValue: String?,
    onProductChipClick: () -> Unit,
    category: CategoryTree?,
    onCategoryClick: () -> Unit,
    priceFrom: BigDecimal?,
    onPriceFromClick: () -> Unit,
    priceTo: BigDecimal?,
    onPriceToClick: () -> Unit,
) {
    Row(
        modifier = modifier.then(
            Modifier.horizontalScroll(rememberScrollState())
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        category?.let {
            FilterChip(
                onClick = onCategoryClick,
                label = { Text("Category: ${it.name}") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        productTemplateChipValue?.let {
            FilterChip(
                onClick = onProductChipClick,
                label = { Text("Product: $it") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        priceFrom?.let {
            FilterChip(
                onClick = onPriceFromClick,
                label = { Text(stringResource(id = R.string.price_from) + ": $priceFrom") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
        priceTo?.let {
            FilterChip(
                onClick = onPriceToClick,
                label = { Text(stringResource(id = R.string.price_to) + ": $priceTo") },
                selected = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }
    }
}