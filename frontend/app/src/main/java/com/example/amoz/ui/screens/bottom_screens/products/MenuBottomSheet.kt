package com.example.amoz.ui.screens.bottom_screens.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.productScreenBottomSheetMenu
import com.example.amoz.data.NavItem
import com.example.amoz.ui.BottomSheetNavigationRaw

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    onDismissRequest: () -> Unit,
    onClick: (NavItemType) -> Unit,
    menuBottomSheetNavItems: Map<NavItemType, NavItem> = productScreenBottomSheetMenu
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // -------------------- Add/Edit Product --------------------
            BottomSheetNavigationRaw(
                leadingIcon = Icons.Filled.Description,
                text = stringResource(id = R.string.products_add_product),
                onClick = {
                    onDismissRequest()
                    onClick(NavItemType.AddProduct)
                }
            )
            BottomSheetNavigationRaw(
                leadingIcon = Icons.Filled.Layers,
                text = stringResource(id = R.string.products_add_product_variant),
                onClick = {
                    onDismissRequest()
                    onClick(NavItemType.AddProductVariant)
                }
            )
            BottomSheetNavigationRaw(
                leadingIcon = Icons.Filled.ViewInAr,
                text = stringResource(id = R.string.products_add_simple_product),
                onClick = {
                    onDismissRequest()
                    onClick(NavItemType.AddSimpleProduct)
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // -------------------- Category --------------------
            menuBottomSheetNavItems.entries.forEach { (navItemType, navItem) ->
                BottomSheetNavigationRaw(
                    leadingIcon = navItem.icon,
                    text = stringResource(id = navItem.title),
                    onClick = {
                        onDismissRequest()
                        onClick(navItemType)
                    }
                )
            }
        }
    }
}
