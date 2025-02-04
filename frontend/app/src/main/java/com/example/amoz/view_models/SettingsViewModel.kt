package com.example.amoz.view_models

import androidx.lifecycle.viewModelScope
import com.example.amoz.api.enums.AppThemeMode
import com.example.amoz.app.AppPreferences
import com.example.amoz.ui.states.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences
): BaseViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState = _settingsUiState.asStateFlow()

    val currency = appPreferences.currency
    val appThemeMode = appPreferences.themeMode

    fun changeAppThemeMode(appThemeMode: AppThemeMode) {
        viewModelScope.launch {
            appPreferences.saveThemeMode(appThemeMode)
        }
    }

    fun changeCurrency(currency: String) {
        viewModelScope.launch {
            appPreferences.saveCurrency(currency.uppercase())
        }
    }

    fun expandChangeAppThemeModeBottomSheet(expand: Boolean) {
        _settingsUiState.update { it.copy(changeAppThemeMode = expand) }
    }

    fun expandChangeUserCurrencyBottomSheet(expand: Boolean) {
        _settingsUiState.update { it.copy(changeUserCurrency = expand) }
    }
}