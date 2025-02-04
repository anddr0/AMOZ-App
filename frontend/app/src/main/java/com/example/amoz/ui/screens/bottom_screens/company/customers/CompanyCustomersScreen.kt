package com.example.amoz.ui.screens.bottom_screens.company.customers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.ui.components.ResultStateView
import com.example.amoz.view_models.CompanyViewModel
import com.example.amoz.ui.theme.AmozApplicationTheme
import kotlinx.coroutines.launch

@Composable
fun CompanyCustomersScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    companyViewModel: CompanyViewModel,
    callSnackBar: (String, ImageVector?) -> Unit,
) {
    val companyUiState by companyViewModel.companyUIState.collectAsState()

    val tabTitles = listOf(
        stringResource(id = R.string.company_customers_b2c),
        stringResource(id = R.string.company_customers_b2b)
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabTitles.size }
    )

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column {
            // -------------------- TabRow with Pager Synchronization --------------------
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .height(3.dp)
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            // -------------------- HorizontalPager with Content --------------------
            HorizontalPager(
                state = pagerState,
            ) { index ->
                when (index) {
                    0 -> {
                        LaunchedEffect(Lifecycle.Event.ON_RESUME) {
                            companyViewModel.fetchCustomersB2C(true)
                        }
                        ResultStateView(
                            state = companyUiState.companyB2CCustomers,
                            onPullToRefresh = {companyViewModel.fetchCustomersB2B()}
                        ) { customers ->
                            B2CCustomerScreen(
                                navController = navController,
                                b2cCustomersList = customers,
                                companyViewModel = companyViewModel,
                                callSnackBar = callSnackBar
                            )
                        }
                    }
                    1 -> {
                        LaunchedEffect(Lifecycle.Event.ON_RESUME) {
                            companyViewModel.fetchCustomersB2B()
                        }
                        ResultStateView(
                            state = companyUiState.companyB2BCustomers,
                            onPullToRefresh = {companyViewModel.fetchCustomersB2C(true)}
                        ) { customers ->
                            B2BCustomerScreen(
                                navController = navController,
                                b2bCustomersList = customers,
                                companyViewModel = companyViewModel,
                                callSnackBar = callSnackBar,
                            )
                        }
                    }
                }
            }
        }
    }
}
