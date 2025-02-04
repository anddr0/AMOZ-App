package com.example.amoz.functionality_tests

import android.content.Context
import android.util.Log
import androidx.test.filters.LargeTest
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.AttributeCreateRequest
import com.example.amoz.api.requests.DimensionsCreateRequest
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.requests.StockCreateRequest
import com.example.amoz.api.requests.WeightCreateRequest
import com.example.amoz.app.AppPreferences
import com.example.amoz.app.SignOutManager
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.view_models.ProductsViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random


@HiltAndroidTest
@LargeTest
class Products {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @ApplicationContext
    lateinit var context: Context
    @Inject
    lateinit var productRepository: ProductRepository
    @Inject
    lateinit var productVariantRepository: ProductVariantRepository
    @Inject
    lateinit var appPreferences: AppPreferences
    @Inject
    lateinit var signOutManager: SignOutManager
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var testProductsIdList: MutableList<UUID>
    private lateinit var testProductCreateRequest: ProductCreateRequest
    private lateinit var testProductVariantCreateRequest: ProductVariantCreateRequest

    @Before
    fun setup() {
        hiltRule.inject()
        testProductsIdList = mutableListOf()
        testProductCreateRequest = ProductCreateRequest(
            name = "Test Product",
            price = BigDecimal("95.99"),
            categoryId = null,
            description = "Simple test product",
            productAttributes = listOf(
                AttributeCreateRequest(
                    attributeName = "Color",
                    value = "Blue"
                )
            )
        )
        testProductVariantCreateRequest = ProductVariantCreateRequest(
            productID = null,
            variantName = "Test product variant",
            variantPrice = BigDecimal("95.99"),
            productVariantCode = 1650437000,
            stock = StockCreateRequest(15, 5),
            weight = null,
            dimensions = null,
            variantAttributes =  listOf(
                AttributeCreateRequest(
                    attributeName = "Color",
                    value = "Blue"
                )
            )
        )
        productsViewModel = ProductsViewModel(
            context, productRepository, productVariantRepository,
            appPreferences, signOutManager
        )
    }



    @Test
    fun testProductListFetch() {
        productsViewModel.fetchProductsList {
            assertNotNull(it)
        }
    }

    @Test
    fun createValidProduct() {
        val latch = CountDownLatch(1)
        val productToCreate = this.testProductCreateRequest

        productsViewModel.createProduct(productToCreate) { productDetails ->
            val productId = productDetails.productId
            addProductIdToTestData(productId)
            assertProductInList(productId) {
                assert(it)
                latch.countDown()
            }
        }
        latch.await(5, TimeUnit.SECONDS)
    }


    @Test
    fun createInvalidProduct() {
        val invalidProductsList = listOf(
            testProductCreateRequest.copy(name = null),
            testProductCreateRequest.copy(name = "test".repeat(50)),
            testProductCreateRequest.copy(price = null),
            testProductCreateRequest.copy(price = BigDecimal("0.123")),
            testProductCreateRequest.copy(price = BigDecimal("-123")),
            testProductCreateRequest.copy(description = "test".repeat(1000)),
            testProductCreateRequest.copy(brand = "test".repeat(100)),
            testProductCreateRequest.copy(productAttributes =
            listOf(
                AttributeCreateRequest(
                    attributeName = "Color",
                    value = "Blue"
                ),
                AttributeCreateRequest(
                    attributeName = "Color",
                    value = "Blue"
                )
            )
            ),
        )

        invalidProductsList.forEach { productToCreate ->
            Log.d("Current product create request", productToCreate.toString())
            assertThrows(IllegalArgumentException::class.java) {
                productsViewModel.createProduct(productToCreate) {
                    addProductIdToTestData(it.productId)
                }
            }
        }
    }

    @Test
    fun modifyProduct() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest
        val nameToUpdate = "Updated product"
        val priceToUpdate = BigDecimal(15.00)
        val attributesToUpdate = listOf(
            AttributeCreateRequest(
                attributeName = "Color",
                value = "Blue"
            ),
            AttributeCreateRequest(
                attributeName = "Memory",
                value = "128 Gb"
            )
        )

        productsViewModel.createProduct(productToCreate) { product ->
            addProductIdToTestData(product.productId)

            productsViewModel.updateProduct(product.productId,
                testProductCreateRequest.copy(
                    name = nameToUpdate,
                    price = priceToUpdate,
                    productAttributes = attributesToUpdate
                )
            ) { updatedProduct ->
                getProduct(product.productId) {
                    assertEquals(nameToUpdate, updatedProduct.name)
                    assertEquals(priceToUpdate, updatedProduct.price)
                    assertEquals(attributesToUpdate.count(), updatedProduct.productAttributes.count())
                    latch.countDown()
                }
            }
        }
        latch.await(5, TimeUnit.SECONDS)
    }

    @Test
    fun deleteProductWithProductVariants() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest
        productsViewModel.createProduct(productToCreate) { product ->
            productsViewModel.deleteProduct(product.productId) {
                assertProductInList(product.productId) {
                    assert(!it)
                    latch.countDown()
                }
            }
        }
        latch.await(5, TimeUnit.SECONDS)
    }

    // -------------------- Product Variant--------------------
    @Test
    fun createValidProductVariant() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest

        productsViewModel.createProduct(productToCreate) { product ->
            val productVariantToCreate = testProductVariantCreateRequest.copy(productID = product.productId)
            addProductIdToTestData(product.productId)
            productsViewModel.createProductVariant(productVariantToCreate){ productVariant ->

                assertProductVariantInList(product.productId, productVariant.productVariantId) {
                    assert(it)
                    latch.countDown()
                }
            }
        }
        latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    fun createInvalidProductVariant() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest

        productsViewModel.createProduct(productToCreate) { product ->
            addProductIdToTestData(product.productId)
            val invalidProductVariantsList = listOf(
                testProductVariantCreateRequest.copy(productID = null),
                testProductVariantCreateRequest.copy(variantName = null),
                testProductVariantCreateRequest.copy(variantName = "Test".repeat(1000)),
                testProductVariantCreateRequest.copy(variantPrice = null),
                testProductVariantCreateRequest.copy(variantPrice = BigDecimal(-01.50)),
                testProductVariantCreateRequest.copy(variantPrice = BigDecimal(01.123)),
                testProductVariantCreateRequest.copy(productVariantCode = null),
                testProductVariantCreateRequest.copy(productVariantCode = -123),
                testProductVariantCreateRequest.copy(stock = null),
                testProductVariantCreateRequest.copy(stock = StockCreateRequest(-1)),
                testProductVariantCreateRequest.copy(stock = StockCreateRequest()),
            )
            invalidProductVariantsList.forEach { productVariantToCreate ->
                Log.i("createInvalidProductVariant", productVariantToCreate.toString())
                assertThrows(IllegalArgumentException::class.java) {
                    productsViewModel.createProductVariant(productVariantToCreate)
                }
            }
            latch.countDown()
        }
        latch.await(10, TimeUnit.SECONDS)
    }

    @Test
    fun modifyProductVariant() {
        // Przygotowanie narzędzia do oczekiwania na zakończenie operacji (CountDownLatch)
        val latch = CountDownLatch(1)

        // Ustawienie nowych wartości dla aktualizacji wariantu produktu
        val nameToUpdate = "Updated product variant" // Nowa nazwa wariantu produktu
        val priceToUpdate = BigDecimal(15.00) // Nowa cena produktu
        val stockAmountToUpdate = 3 // Nowa dostępna ilość produktu w magazynie
        val attributesToUpdate = listOf( // Nowe atrybuty dla wariantu produktu
            AttributeCreateRequest(
                attributeName = "Color", // Nazwa atrybutu (np. "Kolor")
                value = "Blue" // Wartość atrybutu (np. "Niebieski")
            ),
            AttributeCreateRequest(
                attributeName = "Memory", // Nazwa atrybutu (np. "Pamięć")
                value = "128 Gb" // Wartość atrybutu (np. "128 GB")
            )
        )

        // Tworzenie produktu
        productsViewModel.createProduct(testProductCreateRequest) { product ->
            // Tworzenie nowego wariantu produktu na podstawie utworzonego produktu
            val productVariantToCreate = testProductVariantCreateRequest.copy(productID = product.productId)

            // Dodanie ID produktu do danych testowych (np. dla późniejszego usunięcia)
            addProductIdToTestData(product.productId)

            // Tworzenie wariantu produktu
            productsViewModel.createProductVariant(productVariantToCreate) { productVariant ->
                // Przygotowanie danych do aktualizacji wariantu
                val productVariantToUpdate = productVariantToCreate.copy(
                    variantName = nameToUpdate, // Nowa nazwa wariantu
                    variantPrice = priceToUpdate, // Nowa cena
                    stock = StockCreateRequest(stockAmountToUpdate, 5), // Nowy stan magazynowy
                    variantAttributes = attributesToUpdate, // Nowe atrybuty
                )

                // Aktualizacja wariantu produktu
                productsViewModel.updateProductVariant(
                    productVariant.productVariantId, productVariantToUpdate
                ) { updatedProductVariant ->
                    // Sprawdzanie, czy dane zostały zaktualizowane poprawnie
                    // Sprawdzenie nazwy
                    assertEquals(nameToUpdate, updatedProductVariant.variantName)
                    // Sprawdzenie ceny
                    assertEquals(priceToUpdate, updatedProductVariant.variantPrice)
                    // Sprawdzenie ilości w magazynie
                    assertEquals(stockAmountToUpdate, updatedProductVariant.stock.amountAvailable)
                    // Sprawdzenie alarmu magazynowego
                    assertEquals(true, updatedProductVariant.stock.isAlarmTriggered)
                    // Sprawdzenie ilości atrybutów
                    assertEquals(attributesToUpdate.size, updatedProductVariant.variantAttributes.size)

                    // Informacja, że test może zakończyć oczekiwanie
                    latch.countDown()
                }
            }
        }

        // Oczekiwanie na zakończenie operacji (maksymalnie 1 sekunda)
        latch.await(1, TimeUnit.SECONDS)
    }



    @Test
    fun deleteProductVariant() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest

        productsViewModel.createProduct(productToCreate) { product ->
            val productVariantToCreate = testProductVariantCreateRequest.copy(productID = product.productId)
            addProductIdToTestData(product.productId)
            productsViewModel.createProductVariant(productVariantToCreate){ productVariant ->

                productsViewModel.deleteProductVariant(productVariant.productVariantId) {
                    assertProductVariantInList(product.productId, productVariant.productVariantId) {
                        assert(!it)
                        latch.countDown()
                    }
                }
            }
        }
        latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    fun setProductMainVariant() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest

        productsViewModel.createProduct(productToCreate) { product ->
            val productVariantToCreate = testProductVariantCreateRequest.copy(productID = product.productId)
            addProductIdToTestData(product.productId)
            productsViewModel.createProductVariant(productVariantToCreate){ productVariant ->

                productsViewModel.setProductMainVariant(product.productId, productVariant.productVariantId) {
                    getProduct(product.productId) {
                        assert(it?.mainProductVariant?.productVariantId == productVariant.productVariantId)
                    }
                }
            }
        }
        latch.await(1, TimeUnit.SECONDS)
    }


    @Test
    fun createSimpleProduct() {
        val latch = CountDownLatch(1)
        val productToCreate = testProductCreateRequest
        val productVariantToCreate = testProductVariantCreateRequest

        productsViewModel.createSimpleProduct(productToCreate, productVariantToCreate) { product, productVariant ->
            addProductIdToTestData(product.productId)

            assertProductInList(product.productId) {
                assert(it)
            }
            assertProductVariantInList(product.productId, productVariant.productVariantId) {
                assert(it)
            }
            latch.countDown()
        }

        latch.await(1, TimeUnit.SECONDS)
    }

    @After
    fun clearTestData() {
        testProductsIdList.forEach { productsViewModel.deleteProduct(it) }
    }

    private fun assertProductVariantInList(
        productId: UUID,
        productVariantId: UUID,
        isFound: (Boolean) -> Unit) {
        productsViewModel.fetchProductVariantsList(productId) { productVariantsList ->
            val foundProduct = productVariantsList.any { it.productVariantId == productVariantId }
            isFound(foundProduct)
        }
    }

    private fun assertProductInList(productId: UUID, isFound: (Boolean) -> Unit) {
        productsViewModel.fetchProductsList { productsList ->
            val foundProduct = productsList.any { it.productId == productId }
            isFound(foundProduct)
        }
    }

    private fun getProduct(productId: UUID, foundProduct: (ProductSummary?) -> Unit) {
        productsViewModel.fetchProductsList { productsList ->
            val product = productsList.find { it.productId == productId }
            foundProduct(product)
        }
    }


    private fun addProductIdToTestData(productId: UUID) {
        testProductsIdList.add(productId)
    }

}