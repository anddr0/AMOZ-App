package com.example.amoz.ui

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.amoz.R
import com.example.amoz.data.NavItem
import com.example.amoz.navigation.AppNavigationHost
import com.example.amoz.navigation.NavItemType
import com.example.amoz.navigation.allApplicationScreensMap
import com.example.amoz.navigation.bottomNavigationBarNavItemsMap
import com.example.amoz.navigation.otherNavigationItemsMap
import com.example.amoz.pickers.SavedStateHandleKeys
import com.example.amoz.ui.components.CustomSnackBar
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.screens.more_button.MoreBottomSheet
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.AppViewModel
import com.example.amoz.view_models.AuthenticationViewModel
import com.example.amoz.view_models.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainScaffold(
    appViewModel: AppViewModel = hiltViewModel(),
    bottomNavigationItems: List<NavItem> = bottomNavigationBarNavItemsMap.values.toList(),
    navigationController: NavHostController = rememberNavController(),
    onUserAuthorizationCheck: () -> Unit,
) {
    val appUiState by appViewModel.appUiState.collectAsState()

    val appThemeMode by appViewModel.appThemeMode.collectAsState()

    val currentRoute = navigationController.currentBackStackEntryAsState().value?.destination?.route
    val showNavElements = navigationController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("showNavElements")
    val currentNavigationItem = allApplicationScreensMap.values.find { it.screenRoute == currentRoute }

    if (currentNavigationItem != null) {
        appViewModel.updateCurrentNavItem(currentNavigationItem, showNavElements)
    }

    LaunchedEffect(Unit) {
        Log.d("PICKER MODES", navigationController.previousBackStackEntry?.savedStateHandle?.get<Boolean>(
            SavedStateHandleKeys.CATEGORY_PICKER_MODE).toString())
        Log.d("PICKER MODES", navigationController.previousBackStackEntry?.savedStateHandle?.get<Boolean>(
            SavedStateHandleKeys.CATEGORY_PICKER_MODE_LEAVES_ONLY).toString())
    }

    //hardcoded state values, implement normal logic for them
    var showTopBar by remember { mutableStateOf(true) }
    var showBackArrow by remember { mutableStateOf(false) }

    navigationController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            Screens.Entry.route -> {
                appViewModel.setNavigationVisibility(false)
                showTopBar = false
                showBackArrow = false
            }
            Screens.NoCompany.route -> {
                appViewModel.setNavigationVisibility(false)
                showTopBar = true
                showBackArrow = false
            }
            Screens.Register.route -> {
                appViewModel.setNavigationVisibility(false)
                showTopBar = true
                showBackArrow = false
            }
            else -> {
                showTopBar = true
                showBackArrow = true
            }
        }
    }

    LaunchedEffect(Unit) {
        onUserAuthorizationCheck()
    }

    fun navigateToScreen(navigationItem: NavItem, showBars: Boolean = true) {
        appViewModel.setNavigationVisibility(showBars)
        if (currentNavigationItem != navigationItem) {
            navigationController.navigate(navigationItem.screenRoute) {
                popUpTo(navigationController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val coroutineShape = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarIcon by remember { mutableStateOf<ImageVector?>(null) }

    fun callSnackBar(text: String, leadingIcon: ImageVector? = null) {
        snackBarIcon = leadingIcon
        coroutineShape.launch {
            snackBarHostState.showSnackbar(text)
            snackBarIcon = null
        }
    }
    AmozApplicationTheme(appThemeMode) {
        Scaffold(
            // -------------------- TOP BAR --------------------
            topBar = {
                if (showTopBar) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = appUiState.currentNavigationItem?.let
                                { stringResource(it.title) } ?: "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            if (!appUiState.appNavigationVisibility && showBackArrow) {
                                IconButton(onClick = { navigationController.navigateUp() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        actions = {
                            if (appUiState.appNavigationVisibility) {
                                IconButton(
                                    onClick = {
                                        navigationController.navigate(
                                            otherNavigationItemsMap[NavItemType.Profile]!!.screenRoute
                                        )
                                    }) {
                                    Icon(
                                        imageVector = otherNavigationItemsMap[NavItemType.Profile]!!.icon,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                    )
                }
            },
            // -------------------- BOTTOM NAVIGATION BAR --------------------
            bottomBar = {
                if (appUiState.appNavigationVisibility) {
                    NavigationBar {
                        bottomNavigationItems.forEach { item ->
                            NavigationBarItem(
                                selected = appUiState.currentNavigationItem == item,
                                onClick = { navigateToScreen(item) },
                                label = { Text(
                                    text = stringResource(item.title),
                                    maxLines = 1,
                                ) },
                                alwaysShowLabel = true,
                                icon = {
                                    Icon(
                                        imageVector =
                                        if (appUiState.currentNavigationItem == item) {
                                            item.icon
                                        } else item.unselectedIcon ?: item.icon,
                                        contentDescription = stringResource(item.title),
                                    )
                                }
                            )
                        }

                        // -------------------- 'More' bottom Sheet --------------------
                        NavigationBarItem(
                            selected = false,
                            onClick = { appViewModel.updateMoreBottomSheetVisibility(true) },
                            label = { Text(text = stringResource(R.string.more_screen)) },
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.MoreHoriz,
                                    contentDescription = null,
                                )
                            }
                        )

                    }
                }
            },
            // -------------------- Custom Snack Bar --------------------
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) {
                    CustomSnackBar(
                        message = it.visuals.message,
                        leadingIcon = snackBarIcon
                    )
                }
            }
        ) { paddingValues ->
            // -------------------- App NavHost --------------------
            AppNavigationHost(
                navController = navigationController,
                paddingValues = paddingValues,
                callSnackBar = { text, icon -> callSnackBar(text = text, leadingIcon = icon) },
                navigateToScreen = { navigateToScreen(it) },
            )

            // -------------------- 'More' Bottom Sheet --------------------
            if (appUiState.moreBottomSheetIsVisible) {
                MoreBottomSheet(
                    hideMoreBottomSheet = { appViewModel.updateMoreBottomSheetVisibility(false) },
                    navigateToScreen = { navItem -> navigateToScreen(navItem) }
                )
            }
        }
    }
}