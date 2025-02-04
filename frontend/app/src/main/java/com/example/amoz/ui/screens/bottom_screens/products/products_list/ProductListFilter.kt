package com.example.amoz.ui.screens.bottom_screens.products.products_list

import com.example.amoz.models.CategoryTree
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.view_models.ProductsViewModel
import java.math.BigDecimal

class ProductListFilter {
    fun filterProducts(
        templates: List<ProductSummary>,
        searchQuery: String,
        filterParams: ProductsViewModel.FilterParams,
    ): List<ProductSummary> {
        return templates
            .filter { product ->
                // Filter by search query
                (searchQuery.takeIf { it.isNotBlank() }?.let {
                    product.name.contains(it, ignoreCase = true) ||
                    product.brand?.contains(it, ignoreCase = true) ?: true
                } ?: true) &&
                    // Filter by category
                    (filterParams.category?.let {
                        isCategoryMatched(it, product.category?.name)
                    } ?: true) &&
                        // Filter by base price
                    (product.price in
                            (filterParams.priceFrom ?: BigDecimal.ZERO)..
                            (filterParams.priceTo ?: BigDecimal(Int.MAX_VALUE)))
            }
            .sortedWith(
                when (filterParams.sortingType) {
                    ProductsViewModel.SortingType.ASCENDING_NAME -> compareBy { it.name }
                    ProductsViewModel.SortingType.DESCENDING_NAME -> compareByDescending { it.name }
                    ProductsViewModel.SortingType.ASCENDING_PRICE -> compareBy { it.price }
                    ProductsViewModel.SortingType.DESCENDING_PRICE -> compareByDescending { it.price }
                    ProductsViewModel.SortingType.NONE -> compareBy { it.productId } // Default or no sorting
                }
            )
    }

    fun filterProductVariants(
        variants: List<ProductVariantSummary>,
        searchQuery: String,
        filterParams: ProductsViewModel.FilterParams,
    ): List<ProductVariantSummary> {
        return variants
            .filter { variant ->
                // Filter by search query
                (searchQuery.takeIf { it.isNotBlank() }
                    ?.let { variant.variantName?.contains(it, ignoreCase = true) } ?: true) &&
                        // Filter by price range
                        (variant.variantPrice in
                                (filterParams.priceFrom ?: BigDecimal.ZERO)..
                                (filterParams.priceTo ?: BigDecimal(Int.MAX_VALUE)))
            }
            .sortedWith(
                when (filterParams.sortingType) {
                    ProductsViewModel.SortingType.ASCENDING_NAME -> compareBy { it.variantName }
                    ProductsViewModel.SortingType.DESCENDING_NAME -> compareByDescending { it.variantName }
                    ProductsViewModel.SortingType.ASCENDING_PRICE -> compareBy { it.variantPrice }
                    ProductsViewModel.SortingType.DESCENDING_PRICE -> compareByDescending { it.variantPrice }
                    ProductsViewModel.SortingType.NONE -> compareBy { it.productVariantId } // Default or no sorting
                }
            )
    }

    private fun isCategoryMatched(filterCategory: CategoryTree?, productCategory: String?): Boolean {
        if (filterCategory == null) return true

        if (filterCategory.name == productCategory) return true

        for (child in filterCategory.childCategories) {
            if (isCategoryMatched(child, productCategory)) {
                return true
            }
        }

        return false
    }

}
