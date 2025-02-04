package com.example.amoz.pickers

import androidx.navigation.NavController
import com.example.amoz.models.CustomerAnyRepresentation
import com.example.amoz.models.CustomerB2B
import com.example.amoz.models.CustomerB2C
import com.example.amoz.ui.screens.Screens

class CustomerPicker(navController: NavController)
    : BasePicker<CustomerAnyRepresentation>(navController, CustomerAnyRepresentation.serializer()
    ){

    fun isCustomerPickerMode(): Boolean {
        return getMode(SavedStateHandleKeys.CUSTOMER_PICKER_MODE)
    }

    fun getPickedCustomer(): CustomerAnyRepresentation? {
        return getPickedItem(SavedStateHandleKeys.PICKED_CUSTOMER_REPRESENTATION)
    }

    fun removePickedCustomer() {
        removePickedItem(SavedStateHandleKeys.PICKED_CUSTOMER_REPRESENTATION)
    }

    fun navigateToCustomerScreen() {
        setCustomerPickerMode(true)
        navController.navigate(Screens.Customers.route)
    }

    fun pickCustomer(customer: CustomerB2B) {
        pickItem(SavedStateHandleKeys.PICKED_CUSTOMER_REPRESENTATION, CustomerAnyRepresentation(customer))
        pickCustomer()
    }

    fun pickCustomer(customer: CustomerB2C) {
        pickItem(SavedStateHandleKeys.PICKED_CUSTOMER_REPRESENTATION, CustomerAnyRepresentation(customer))
        pickCustomer()
    }

    private fun pickCustomer() {
        navController.popBackStack()
        setCustomerPickerMode(false)
        removePickedCustomer()
    }

    private fun setCustomerPickerMode(mode: Boolean) {
        currentSavedStateHandleSetMode(SavedStateHandleKeys.CUSTOMER_PICKER_MODE, mode)
    }
}

