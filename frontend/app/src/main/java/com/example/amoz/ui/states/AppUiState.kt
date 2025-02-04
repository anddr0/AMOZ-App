package com.example.amoz.ui.states

import com.example.amoz.data.NavItem
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.bottomNavigationBarNavItemsMap

data class AppUiState (
    val appNavigationVisibility: Boolean = true,
    val moreBottomSheetIsVisible: Boolean = false,
    val currentNavigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home],
)