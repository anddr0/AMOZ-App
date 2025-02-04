package com.example.amoz.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.amoz.api.enums.AppThemeMode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

class AppPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app_preferences")

        private val CURRENCY_KEY = stringPreferencesKey("currency")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }

    private val _currency = MutableStateFlow("USD")
    val currency: StateFlow<String> = _currency

    private val _themeMode = MutableStateFlow(AppThemeMode.Auto)
    val themeMode: StateFlow<AppThemeMode> = _themeMode

    init {
        // Currency flow
        context.dataStore.data
            .map { preferences -> preferences[CURRENCY_KEY] ?: "USD" }
            .onEach { _currency.value = it }
            .launchIn(CoroutineScope(Dispatchers.IO))

        // Theme mode flow
        context.dataStore.data
            .map { preferences ->
                when (preferences[THEME_MODE_KEY]) {
                    "Dark" -> AppThemeMode.Dark
                    "Light" -> AppThemeMode.Light
                    else -> AppThemeMode.Auto
                }
            }
            .onEach { _themeMode.value = it }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    suspend fun saveCurrency(currencyCode: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = currencyCode
        }
    }

    suspend fun saveThemeMode(themeMode: AppThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSignOutManager(): SignOutManager {
        return SignOutManager()
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }
}

