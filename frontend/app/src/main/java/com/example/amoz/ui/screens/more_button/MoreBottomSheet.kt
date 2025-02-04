package com.example.amoz.ui.screens.more_button

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.amoz.data.NavItem
import com.example.amoz.navigation.moreBottomSheetItemsMap
import com.example.amoz.ui.BottomSheetNavigationRaw


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBottomSheet(
    hideMoreBottomSheet: () -> Unit,
    navigateToScreen: (NavItem) -> Unit,
    moreBottomSheetNavItems: List<NavItem> = moreBottomSheetItemsMap.values.toList()
) {
    ModalBottomSheet(
        onDismissRequest = { hideMoreBottomSheet() }
    )
    {
        Column {
            moreBottomSheetNavItems.forEach { item ->
                BottomSheetNavigationRaw(
                    leadingIcon = item.icon,
                    text = stringResource(item.title),
                    onClick = {
                        hideMoreBottomSheet()
                        navigateToScreen(item)
                    }
                )
            }
        }
    }
}
