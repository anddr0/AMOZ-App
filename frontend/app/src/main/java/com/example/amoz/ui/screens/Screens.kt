package com.example.amoz.ui.screens

sealed class Screens(val route : String) {
    // --------------------- Bottom Navigation Bar Screens ---------------------
    data object Home : Screens("home_route")
    data object Products : Screens("products_route")
    data object Orders : Screens("orders_route")
    data object Company : Screens("company_route")

    // --------------------- Products Screen ---------------------
    data object Categories : Screens("products_categories_route")

    // --------------------- 'More' Button Screens ---------------------
    data object Settings : Screens("settings_route")
    data object FAQ : Screens("faq_route")
    data object About : Screens("about_route")

    // --------------------- Entry Screens ---------------------
    data object Entry : Screens("entry_route")
    data object Register : Screens("register_route")
    data object RegisterImage : Screens("register_image_route")

    // --------------------- Orders Screens ---------------------
    data object OrdersAddEdit : Screens("order_add_edit_route")

    // --------------------- Other Bar Screens ---------------------
    data object Profile : Screens("profile_route")
    data object ProfileEdit : Screens("profile_edit_route")

    // --------------------- Company Info Screens ---------------------
    data object Employees : Screens("employees_route")
    data object Customers : Screens("customers_route")

    data object NoCompany : Screens("no_company_route")
    data object CreateCompany : Screens("create_company_route")

}