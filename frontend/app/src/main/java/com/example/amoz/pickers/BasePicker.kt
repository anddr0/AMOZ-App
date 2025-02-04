package com.example.amoz.pickers

import androidx.navigation.NavController
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class BasePicker<T> (
    protected val navController: NavController,
    private val serializer: KSerializer<T>
) {
    protected val currentSavedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    protected val previousSavedStateHandle = navController.previousBackStackEntry?.savedStateHandle

    protected fun pickItem(key: String, item: T) {
        previousSavedStateHandle?.set(key, encodeToJson(item))
    }

    protected fun getPickedItem(key: String): T? {
        return currentSavedStateHandle?.get<String>(key)?.let {decodeFromJson(it)}
    }

    protected fun removePickedItem(key: String) {
        currentSavedStateHandle?.remove<String>(key)
    }

    protected fun encodeToJson(data: T): String {
        return Json.encodeToString(serializer, data)
    }

    protected fun decodeFromJson(json: String): T {
        return Json.decodeFromString(serializer, json)
    }

    protected fun setNavElementsVisibleMode(mode: Boolean) {
        currentSavedStateHandleSetMode(SavedStateHandleKeys.SHOW_NAV_ELEMENTS, mode)
    }

    protected fun previousSavedStateHandleSetMode(key: String, mode: Boolean) {
        previousSavedStateHandle?.set(key, mode)
    }

    protected fun currentSavedStateHandleSetMode(key: String, mode: Boolean) {
        currentSavedStateHandle?.set(key, mode)
    }

    protected fun getMode(key: String): Boolean {
        return previousSavedStateHandle?.get<Boolean>(key) ?: false
    }
}