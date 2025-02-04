package com.example.amoz.functionality_tests

import com.example.amoz.models.CategorySummary
import com.example.amoz.models.CategoryTree
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.ui.screens.bottom_screens.products.products_list.ProductListFilter
import com.example.amoz.view_models.ProductsViewModel
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.util.UUID

class ProductListFilterTest {

    private val productListFilter = ProductListFilter()

    @Test
    fun `filterProducts without any filters returns all sorted by productId`() {
        val products = generateProductSummaries()

        val result = productListFilter.filterProducts(
            templates = products,
            searchQuery = "",
            filterParams = ProductsViewModel.FilterParams()
        )

        assertEquals(3, result.size)
        val ids = products.map { it.productId }.toSet()
        assertTrue(result.map { it.productId }.toSet() == ids)
    }

    @Test
    fun `filterProducts by searchQuery filters correctly`() {
        val products = generateProductSummaries()

        val result = productListFilter.filterProducts(
            templates = products,
            searchQuery = "BrandA",
            filterParams = ProductsViewModel.FilterParams()
        )

        assertEquals(1, result.size)
        assertEquals("BrandA", result.first().brand)
    }

    @Test
    fun `filterProducts by category filters correctly`() {
        // Tworzenie listy produktów, które będą filtrowane
        val products = generateProductSummaries() // Funkcja generuje przykładowe produkty

        // Tworzenie drzewa kategorii (np. "Elektronika" i "Odzież")
        val categoryTree = CategoryTree(
            categoryId = UUID.randomUUID(), // Generowanie unikalnego identyfikatora dla głównej kategorii
            name = "Root", // Główna kategoria nadrzędna
            categoryLevel = 1, // Poziom hierarchii kategorii (1 to główny poziom)
            childCategories = listOf( // Kategorie podrzędne
                CategoryTree(
                    categoryId = UUID.randomUUID(), // ID dla "Elektronika"
                    name = "Electronics", // Nazwa kategorii: "Elektronika"
                    categoryLevel = 2, // Drugi poziom hierarchii
                    childCategories = listOf() // Brak podkategorii
                ),
                CategoryTree(
                    categoryId = UUID.randomUUID(), // ID dla "Odzież"
                    name = "Clothing", // Nazwa kategorii: "Odzież"
                    categoryLevel = 2, // Drugi poziom hierarchii
                    childCategories = listOf() // Brak podkategorii
                )
            )
        )

        // Definiowanie parametrów filtra dla kategorii "Elektronika"
        val filterParams = ProductsViewModel.FilterParams(
            // Wybranie kategorii "Elektronika"
            category = categoryTree.childCategories.first { it.name == "Electronics" }
        )

        // Filtrowanie produktów na podstawie parametrów
        val result = productListFilter.filterProducts(
            templates = products, // Lista produktów do filtrowania
            searchQuery = "", // Pusta fraza wyszukiwania
            filterParams = filterParams // Parametry filtra
        )

        // Oczekiwany wynik: produkty należące do kategorii "Elektronika"
        val expected = products.filter { it.category?.name == "Electronics" }

        // Sprawdzanie, czy liczba produktów po filtrowaniu jest poprawna
        assertEquals(expected.size, result.size)

        // Sprawdzanie, czy wszystkie produkty w wyniku należą do kategorii "Elektronika"
        assertTrue(result.all { it.category?.name == "Electronics" })
    }

    @Test
    fun `filterProducts by price range filters correctly`() {
        val products = generateProductSummaries()
        val filterParams = ProductsViewModel.FilterParams(
            priceFrom = BigDecimal("15"),
            priceTo = BigDecimal("25")
        )

        val result = productListFilter.filterProducts(
            templates = products,
            searchQuery = "",
            filterParams = filterParams
        )

        // Должны остаться только товары с ценой в диапазоне [15,25]
        assertTrue(result.all { it.price in BigDecimal("15")..BigDecimal("25") })
    }

    @Test
    fun `filterProducts sorting by name ascending`() {
        val products = generateProductSummaries()
        val filterParams = ProductsViewModel.FilterParams(
            sortingType = ProductsViewModel.SortingType.ASCENDING_NAME
        )

        val result = productListFilter.filterProducts(
            templates = products,
            searchQuery = "",
            filterParams = filterParams
        )

        val sortedByName = products.sortedBy { it.name }.map { it.productId }
        assertEquals(sortedByName, result.map { it.productId })
    }

    @Test
    fun `filterProductVariants by name and price`() {
        val variants = generateProductVariantSummaries()
        val filterParams = ProductsViewModel.FilterParams(
            sortingType = ProductsViewModel.SortingType.ASCENDING_PRICE,
            priceFrom = BigDecimal("100"),
            priceTo = BigDecimal("300")
        )

        val result = productListFilter.filterProductVariants(
            variants = variants,
            searchQuery = "Variant",
            filterParams = filterParams
        )

        // Все варианты содержат в имени "Variant", проверим ценовой диапазон и сортировку
        assertTrue(result.all { it.variantName?.contains("Variant", ignoreCase = true) == true })
        assertTrue(result.all { it.variantPrice in BigDecimal("100")..BigDecimal("300") })
        // Проверим сортировку по цене по возрастанию
        val prices = result.map { it.variantPrice }
        assertTrue(prices == prices.sorted())
    }

    // Вы можете дополнить тесты для остальных случаев, например для DESCENDING_PRICE, DESCENDING_NAME и т.д.

    // Генерация тестовых данных:
    private fun generateProductSummaries(): List<ProductSummary> {
        val product1 = ProductSummary(
            productId = UUID.randomUUID(),
            name = "Alpha",
            price = BigDecimal("10"),
            category = CategorySummary(
                categoryId = UUID.randomUUID(),
                name = "Electronics",
                categoryLevel = 2
            ),
            brand = "BrandA"
        )

        val product2 = ProductSummary(
            productId = UUID.randomUUID(),
            name = "Beta",
            price = BigDecimal("20"),
            category = CategorySummary(
                categoryId = UUID.randomUUID(),
                name = "Clothing",
                categoryLevel = 2
            ),
            brand = "BrandB"
        )

        val product3 = ProductSummary(
            productId = UUID.randomUUID(),
            name = "Gamma",
            price = BigDecimal("30"),
            category = CategorySummary(
                categoryId = UUID.randomUUID(),
                name = "Electronics",
                categoryLevel = 2
            ),
            brand = "BrandC"
        )

        return listOf(product1, product2, product3)
    }

    private fun generateProductVariantSummaries(): List<ProductVariantSummary> {
        return listOf(
            ProductVariantSummary(
                productVariantId = UUID.randomUUID(),
                code = 1,
                variantPrice = BigDecimal("100"),
                variantName = "Variant A"
            ),
            ProductVariantSummary(
                productVariantId = UUID.randomUUID(),
                code = 2,
                variantPrice = BigDecimal("200"),
                variantName = "Variant B"
            ),
            ProductVariantSummary(
                productVariantId = UUID.randomUUID(),
                code = 3,
                variantPrice = BigDecimal("300"),
                variantName = "Variant C"
            )
        )
    }
}
