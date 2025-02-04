package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.screens.Screens

class CategoryPicker(navController: NavController)
    :BasePicker<CategoryTree>(navController, CategoryTree.serializer()) {

    fun isCategoryPickerMode(): Boolean {
        return getMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE)
    }

    fun isCategoryLeavesOnlyPickerMode(): Boolean {
        return getMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE_LEAVES_ONLY)
    }

    fun getPickedCategory(): CategoryTree? {
        return getPickedItem(SavedStateHandleKeys.PICKED_CATEGORY_TREE)
    }

    fun pickCategory(categoryTree: CategoryTree) {
        pickItem(SavedStateHandleKeys.PICKED_CATEGORY_TREE, categoryTree)
        unsetModes()
        navController.popBackStack()
    }

    fun navigateToCategoryScreen(pickLeavesOnly: Boolean) {
        setCategoryPickerMode()
        if (pickLeavesOnly) { setCategoryLeavesOnlyPickerMode() }
        navController.navigate(Screens.Categories.route)
    }

    fun unsetModes() {
        unsetCategoryPickerMode()
        unsetCategoryLeavesOnlyPickerMode()
    }

    fun removePickedCategory() {
        removePickedItem(SavedStateHandleKeys.PICKED_CATEGORY_TREE)
    }

    private fun unsetCategoryPickerMode() {

        previousSavedStateHandleSetMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE, false)
    }

    private fun unsetCategoryLeavesOnlyPickerMode() {
        previousSavedStateHandleSetMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE_LEAVES_ONLY, false)
    }

    private fun setCategoryPickerMode() {
        currentSavedStateHandleSetMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE, true)
    }

    private fun setCategoryLeavesOnlyPickerMode() {
        currentSavedStateHandleSetMode(SavedStateHandleKeys.CATEGORY_PICKER_MODE_LEAVES_ONLY, true)
    }

}