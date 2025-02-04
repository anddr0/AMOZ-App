package com.example.amoz.view_models

import androidx.lifecycle.ViewModel
import com.example.amoz.app.AppPreferences
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.bottomNavigationBarNavItemsMap
import com.example.amoz.data.NavItem
import com.example.amoz.ui.states.AppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : BaseViewModel() {
    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    val appThemeMode = appPreferences.themeMode

    fun updateCurrentNavItem(
        navigationItem: NavItem? = bottomNavigationBarNavItemsMap[NavItemType.Home],
        isNavVisible: Boolean? = null
    ) {
        var isNavElementsVisible = bottomNavigationBarNavItemsMap.values.contains(navigationItem)
        isNavVisible?.let { isNavElementsVisible = it }
        _appUiState.update { currState ->
            currState.copy(
                currentNavigationItem = navigationItem,
                appNavigationVisibility = isNavElementsVisible
            )
        }
    }

    fun setNavigationVisibility(isVisible: Boolean) {
        _appUiState.update { currState ->
            currState.copy(appNavigationVisibility = isVisible)
        }
    }

    fun updateMoreBottomSheetVisibility(isVisible: Boolean) {
        _appUiState.update { currState ->
            currState.copy(moreBottomSheetIsVisible = isVisible)
        }
    }

}