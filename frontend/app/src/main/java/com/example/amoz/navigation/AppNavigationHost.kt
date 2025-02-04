package com.example.amoz.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.amoz.data.NavItem
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.screens.bottom_screens.additional_screens.AboutScreen
import com.example.amoz.ui.screens.bottom_screens.additional_screens.FAQScreen
import com.example.amoz.ui.screens.bottom_screens.additional_screens.settings.SettingsScreen
import com.example.amoz.ui.screens.bottom_screens.company.CompanyScreen
import com.example.amoz.ui.screens.bottom_screens.company.CreateCompanyScreen
import com.example.amoz.ui.screens.bottom_screens.company.NoCompanyScreen
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.ui.screens.bottom_screens.company.customers.CompanyCustomersScreen
import com.example.amoz.ui.screens.bottom_screens.company.employees.CompanyEmployeesScreen
import com.example.amoz.ui.screens.bottom_screens.home.HomeScreen
import com.example.amoz.ui.screens.bottom_screens.orders.OrdersAddEditScreen
import com.example.amoz.ui.screens.bottom_screens.orders.OrdersScreen
import com.example.amoz.ui.screens.bottom_screens.products.ProductScreen
import com.example.amoz.ui.screens.categories.CategoriesScreen
import com.example.amoz.ui.screens.entry.EntryScreen
import com.example.amoz.ui.screens.entry.RegisterImageScreen
import com.example.amoz.ui.screens.entry.RegisterScreen
import com.example.amoz.ui.screens.profile.ProfileEditingScreen
import com.example.amoz.ui.screens.profile.ProfileScreen
import com.example.amoz.view_models.EmployeeViewModel
import com.example.amoz.view_models.OrdersViewModel
import com.example.amoz.view_models.UserViewModel

@Composable
fun AppNavigationHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateToScreen: (NavItem) -> Unit,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    val companyViewModel: CompanyViewModel = hiltViewModel()
    val ordersViewModel: OrdersViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    val employeeViewModel: EmployeeViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screens.Home.route) {

        // -------------------- Bottom navigation screens --------------------
        composable(Screens.Home.route) {
            HomeScreen(
                navigateToScreen = { navigateToScreen(it) },
                navController = navController,
                paddingValues = paddingValues,
                ordersViewModel = ordersViewModel
            ) }
        composable(Screens.Products.route) {
            ProductScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.Orders.route) {
            OrdersScreen(
                navController = navController,
                paddingValues = paddingValues,
                ordersViewModel = ordersViewModel
            ) }
        composable(Screens.Company.route) {
            CompanyScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                paddingValues = paddingValues,
                callSnackBar = callSnackBar,
            ) }

        // -------------------- Products screens --------------------
        composable(Screens.Categories.route) {
            CategoriesScreen(
                paddingValues = paddingValues,
                navController = navController,
            ) }

        // -------------------- 'More' bottom sheet screens --------------------
        composable(Screens.Settings.route) {
            SettingsScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.FAQ.route) {
            FAQScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }
        composable(Screens.About.route) {
            AboutScreen(
                navController = navController,
                paddingValues = paddingValues
            ) }

        // -------------------- Company info screens --------------------

        composable(Screens.Employees.route) {
            CompanyEmployeesScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                paddingValues = paddingValues,
                callSnackBar = callSnackBar,
            )
        }
        composable(Screens.Customers.route) {
            CompanyCustomersScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                paddingValues = paddingValues,
                callSnackBar = callSnackBar,
            ) }

        composable(Screens.NoCompany.route) {
            NoCompanyScreen(
                navController = navController,
                paddingValues = paddingValues,
                companyViewModel = companyViewModel,
            )
        }

        composable(Screens.CreateCompany.route) {
            CreateCompanyScreen(
                navController = navController,
                paddingValues = paddingValues,
                companyViewModel = companyViewModel,
            )
        }

        // -------------------- Profile Screens --------------------
        composable(Screens.Profile.route) {
            ProfileScreen(
                navController = navController,
                paddingValues = paddingValues,
                employeeViewModel = employeeViewModel,
//                authenticationViewModel = authenticationViewModel,
            )
        }

        composable(Screens.ProfileEdit.route) {
            ProfileEditingScreen(
                navController = navController,
                paddingValues = paddingValues,
                employeeViewModel = employeeViewModel
            )
        }

        // -------------------- Entry Screens --------------------
        composable(Screens.Entry.route) {
            EntryScreen(
                navController = navController,
                paddingValues = paddingValues,
                userViewModel = userViewModel
            ) }

        composable(Screens.Register.route) {
            RegisterScreen(
                navController = navController,
                paddingValues = paddingValues,
                userViewModel = userViewModel
            ) }

        composable(Screens.RegisterImage.route) {
            RegisterImageScreen(
                navController = navController,
                paddingValues = paddingValues,
                userViewModel = userViewModel
            )
        }

        // -------------------- Orders Screens --------------------
        composable(Screens.OrdersAddEdit.route) {
            OrdersAddEditScreen(
                navController = navController,
                paddingValues = paddingValues,
                ordersViewModel = ordersViewModel
            ) }

    }
}