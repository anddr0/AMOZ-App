package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.ui.screens.Screens

class ProductVariantPicker(navController: NavController)
    : BasePicker<ProductVariantDetails>(navController, ProductVariantDetails.serializer()) {

    fun isProductVariantPickerMode(): Boolean {
       return getMode(SavedStateHandleKeys.PRODUCT_VARIANT_PICKER_MODE)
    }

    fun getPickedProductVariant(): ProductVariantDetails? {
        return getPickedItem(SavedStateHandleKeys.PICKED_PRODUCT_VARIANT_DETAILS)
    }

    fun removePickedProductVariant() {
        removePickedItem(SavedStateHandleKeys.PICKED_PRODUCT_VARIANT_DETAILS)
    }

    fun navigateToProductScreen() {
        setNavElementsVisibleMode(false)
        setProductVariantPickerMode(true)
        navController.navigate(Screens.Products.route)
    }

    fun pickProductVariant(productVariantDetails: ProductVariantDetails) {
        pickItem(SavedStateHandleKeys.PICKED_PRODUCT_VARIANT_DETAILS, productVariantDetails)
        navController.popBackStack()
        setProductVariantPickerMode(mode = false)
        setNavElementsVisibleMode(mode = true)
    }

    private fun setProductVariantPickerMode(mode: Boolean) {
        currentSavedStateHandleSetMode(SavedStateHandleKeys.PRODUCT_VARIANT_PICKER_MODE, mode)
    }
}