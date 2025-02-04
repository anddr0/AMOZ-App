package com.example.amoz.functionality_tests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.amoz.api.repositories.CustomerRepository
import com.example.amoz.api.repositories.ProductOrderRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.ProductOrderCreateRequest
import com.example.amoz.api.requests.ProductOrderItemCreateRequest
import com.example.amoz.app.AppPreferences
import com.example.amoz.app.SignOutManager
import com.example.amoz.view_models.OrdersViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.update
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class Orders {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var ordersRepository: ProductOrderRepository

    @Inject
    lateinit var customerRepository: CustomerRepository

    @Inject
    lateinit var productsRepository: ProductVariantRepository

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var signOutManager: SignOutManager

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var testOrderCreateRequest: ProductOrderCreateRequest
    private lateinit var testProductOrderItemCreateRequest: ProductOrderItemCreateRequest

    @Before
    fun setup() {
        hiltRule.inject()
        ordersViewModel = OrdersViewModel(
            context = ApplicationProvider.getApplicationContext(),
            ordersRepository,
            productsRepository,
            customerRepository,
            appPreferences,
            signOutManager
        )

        testOrderCreateRequest = ProductOrderCreateRequest()
    }

    @After
    fun clearTestData() {

    }

    @Test
    fun ordersListFetch() {
        ordersViewModel.fetchOrdersList { fetchedList ->
            assertNotNull(fetchedList)
        }
    }

    @Test
    fun testCreateOrder() {
        val orderRequest = ProductOrderCreateRequest()
        val variantItems = listOf<OrdersViewModel.ProductVariantOrderItem>()

        ordersViewModel.createProductOrder(orderRequest, variantItems){ orderDetails ->
            assertNotNull(orderDetails)
        }
    }

    @Test
    fun createInvalidOrder() {
        val orderRequest = ProductOrderCreateRequest()
    }
}