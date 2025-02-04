package com.example.amoz.ui.screens.bottom_screens.additional_screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.amoz.ui.screens.bottom_screens.additional_screens.settings.list_items.AppThemeListItem
import com.example.amoz.ui.screens.bottom_screens.additional_screens.settings.list_items.CurrencyListItem
import com.example.amoz.view_models.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsState()
    val currency by settingsViewModel.currency.collectAsState()
    val appThemeMode by settingsViewModel.appThemeMode.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            CurrencyListItem(currency) {
                settingsViewModel.expandChangeUserCurrencyBottomSheet(true)
            }
            AppThemeListItem(appThemeMode) {
                settingsViewModel.expandChangeAppThemeModeBottomSheet(true)
            }
        }
    }

    if(settingsUiState.changeUserCurrency) {
        ChangeCurrencyBottomSheet(
            currentCurrency = currency,
            onComplete = settingsViewModel::changeCurrency,
            onDismissRequest = {
                settingsViewModel.expandChangeUserCurrencyBottomSheet(false)
            }
        )
    }

    if(settingsUiState.changeAppThemeMode) {
        ChangeAppThemeBottomSheet(
            currentAppThemeMode = appThemeMode,
            onComplete = settingsViewModel::changeAppThemeMode,
            onDismissRequest = {
                settingsViewModel.expandChangeAppThemeModeBottomSheet(false)
            }
        )
    }
}


