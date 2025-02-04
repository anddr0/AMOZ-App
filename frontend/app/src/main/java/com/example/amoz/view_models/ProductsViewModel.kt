package com.example.amoz.view_models

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.repositories.AttributeRepository
import com.example.amoz.api.repositories.ProductRepository
import com.example.amoz.api.repositories.ProductVariantRepository
import com.example.amoz.api.requests.CompanyCreateRequest
import com.example.amoz.api.requests.ProductCreateRequest
import com.example.amoz.api.requests.ProductVariantCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.AppPreferences
import com.example.amoz.app.SignOutManager
import com.example.amoz.extensions.toMultipartBodyPart
import com.example.amoz.models.CategoryTree
import com.example.amoz.models.ProductDetails
import com.example.amoz.models.ProductSummary
import com.example.amoz.models.ProductVariantDetails
import com.example.amoz.models.ProductVariantSummary
import com.example.amoz.ui.states.ProductsUiState
import com.example.amoz.ui.screens.bottom_screens.products.products_list.ProductListFilter
import com.example.amoz.ui.states.CompanyUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val productRepository: ProductRepository,
    private val productVariantRepository: ProductVariantRepository,
    private val attributeRepository: AttributeRepository,
    private val appPreferences: AppPreferences,
    private val signOutManager: SignOutManager,
) : BaseViewModel() {
    private val _productUiState = MutableStateFlow(ProductsUiState())
    val productUiState: StateFlow<ProductsUiState> = _productUiState.asStateFlow()
    private val productFilter = ProductListFilter()

    init {
        observeSignOutEvent()
        viewModelScope.launch {
            _productUiState
                .map { it.filteredByProduct }
                .distinctUntilChanged()
                .collect { filteredProduct ->
                    filteredProduct?.let { fetchProductVariantsList(it.productId) }
                }
        }
    }

    private fun observeSignOutEvent() {
        viewModelScope.launch {
            signOutManager.signOutEvent.collect {
                clearState()
            }
        }
    }

    private fun clearState() {
        _productUiState.update { ProductsUiState() }
    }

    fun fetchProductsListOnScreenLoad() {
        if (_productUiState.value.productsListFetched.value is ResultState.Idle) {
            fetchProductsList()
        }
    }

    // -------------------- PRODUCT --------------------

    fun fetchProductsList(
        binding: MutableStateFlow<ResultState<List<ProductSummary>>>? =
            _productUiState.value.productsListFetched,
        skipLoading: Boolean = false,
        onSuccessCallback: ((List<ProductSummary>) -> Unit)? = null
    ) {
        performRepositoryAction(
            binding = binding,
            failureMessage = "Could not fetch products, try again",
            skipLoading = skipLoading,
            action = { productRepository.getAllProducts() },
            onSuccess = { productsList ->
                binding?.let{ updateProductsList(productsList) }
                onSuccessCallback?.invoke(productsList)
            }
        )
    }

    fun fetchProductDetails(
        productId: UUID,
        onSuccessCallback: ((ProductDetails?) -> Unit)? = null
    ) {
        performRepositoryAction(
            binding = _productUiState.value.productDetailsState,
            failureMessage = "Could not fetch product details, try again",
            action = {
                productRepository.getProductDetails(productId)
            },
            onSuccess = { productDetails ->
                onSuccessCallback?.invoke(productDetails)
            }
        )
    }

    fun createProduct(
        productCreateRequest: ProductCreateRequest,
        onSuccessCallback: ((ProductDetails) -> Unit)? = null,
    ) {
        val validationMessage = productCreateRequest.validate()
        if (validationMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Failed to update product",
                action = {
                    productRepository.createProduct(productCreateRequest)
                },
                onSuccess = { productDetails ->
                    Log.i(tag, "Successfully created product: $productDetails")
                    fetchProductsList(skipLoading = true)
                    onSuccessCallback?.invoke(productDetails)
                }
            )
        } else { throw IllegalArgumentException(validationMessage) }
    }

    fun updateProduct(
        productId: UUID,
        productCreateRequest: ProductCreateRequest,
        onSuccessCallback: ((ProductDetails) -> Unit)? = null,
    ) {
        val validationMessage = productCreateRequest.validate()
        if (validationMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Failed to update product",
                action = {
                    productRepository.updateProduct(
                        productId = productId,
                        request = productCreateRequest
                    )
                },
                onSuccess = { productDetails ->
                    Log.i(tag, "Successfully updated product: $productDetails")
                    fetchProductsList(skipLoading = true)
                    onSuccessCallback?.invoke(productDetails)
                }
            )
        } else { throw IllegalArgumentException(validationMessage) }
    }

    fun deleteProduct(
        productId: UUID,
        onSuccessCallback: (() -> Unit)? = null,
    ) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Failed to delete product",
            action = {
                productRepository.deactivateProduct(productId)
            },
            onSuccess = {
                Log.i(tag, "Successfully deleted product with id: $productId")
                fetchProductsList(skipLoading = true)
                onSuccessCallback?.invoke()
            }
        )
    }

    fun setProductMainVariant(
        productId: UUID, productVariantId: UUID,
        onSuccessCallback: ((ProductDetails) -> Unit)? = null,
    ) {
        performRepositoryAction(
            binding = null,
            action = {
                productRepository.setMainVariant(productId, productVariantId)
            },
            onSuccess = {
                fetchProductsList(skipLoading = true)
                onSuccessCallback?.invoke(it)
            }
        )
    }

    fun fetchAllAttributes() {
        performRepositoryAction(
            binding = _productUiState.value.allAttributesList,
            action = {
                attributeRepository.getAllAttributes()
            }
        )
    }

    fun updateCurrentProductToDelete(productSummary: ProductSummary?) {
        _productUiState.update { it.copy(currentProductToDelete = productSummary) }
    }

    fun saveCurrentProductCreateRequest(productCreateRequest: ProductCreateRequest?) {

        _productUiState.update {
            it.copy(
                productCreateRequest = productCreateRequest
            )
        }
    }

    fun updateCurrentAddEditProduct(productId: UUID?) {
        if (productId == null) {
            _productUiState.update {it.copy(
                productDetailsState = MutableStateFlow(ResultState.Success(null))
            ) }
            saveCurrentProductCreateRequest(null)
        }
        else {
            fetchProductDetails(productId)
        }
    }

    fun updateProductsList(productsList: List<ProductSummary>) {
        _productUiState.update { it.copy(productsList = productsList) }
        applyFilters()
    }

    // -------------------- PRODUCT VARIANT--------------------

    fun fetchProductVariantsList(
        productId: UUID,
        skipLoading: Boolean = false,
        onSuccessCallback: ((List<ProductVariantSummary>) -> Unit)? = null,
    ) {
        performRepositoryAction(
            binding = _productUiState.value.productVariantsListFetched,
            failureMessage = "Could not fetch product variants, try again",
            skipLoading = skipLoading,
            action = { productVariantRepository.getAllProductVariantsByProductId(productId) },
            onSuccess = { productVariantsList ->
                updateProductVariantsList(productVariantsList)
                onSuccessCallback?.invoke(productVariantsList)
            }
        )
    }

    fun updateProductVariantsList(productVariantsList: List<ProductVariantSummary>) {
        _productUiState.update { it.copy(productVariantsList = productVariantsList) }
        applyFilters()
    }

    fun fetchProductVariantDetails(
        productVariantId: UUID,
        binding: MutableStateFlow<ResultState<ProductVariantDetails?>>? =
            _productUiState.value.productVariantDetailsState,
        onSuccessCallback: ((ProductVariantDetails?) -> Unit)? = null,
    ) {
        performRepositoryAction(
            binding = binding,
            failureMessage = "Failed to fetch product variant",
            action = {
                productVariantRepository.getProductVariant(productVariantId)
            },
            onSuccess = { productVariantDetails ->
                _productUiState.value.filteredByProduct?.let {
                    fetchProductVariantsList(it.productId, true)
                }
                onSuccessCallback?.invoke(productVariantDetails)
            }
        )
    }

    fun createProductVariant(
        productVariantCreateRequest: ProductVariantCreateRequest,
        onSuccessCallback: ((ProductVariantDetails) -> Unit)? = null,
    ) {
        val validationMessage = productVariantCreateRequest.validate()
        if (validationMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Failed to create product variant",
                action = {
                    productVariantRepository.createProductVariant(productVariantCreateRequest)
                },
                onSuccess = { productVariantDetails ->
                    _productUiState.value.filteredByProduct?.let {
                        fetchProductVariantsList(it.productId, true)
                    }
                    onSuccessCallback?.invoke(productVariantDetails)
                }
            )
        }
        else { throw IllegalArgumentException(validationMessage) }
    }

    fun updateProductVariant(
        productVariantId: UUID,
        productVariantCreateRequest: ProductVariantCreateRequest,
        onSuccessCallback: ((ProductVariantDetails) -> Unit)? = null,
    ) {
        val validationMessage = productVariantCreateRequest.validate()
        if (validationMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Failed to update product variant",
                action = {
                    productVariantRepository.updateProductVariant(productVariantId, productVariantCreateRequest)
                },
                onSuccess = { productVariantDetails ->
                    productVariantCreateRequest.productID?.let {
                        fetchProductVariantsList(it, true)
                    }
                    onSuccessCallback?.invoke(productVariantDetails)
                }
            )
        }
        else { throw IllegalArgumentException(validationMessage) }
    }

    fun fetchProductVariantPicture(
        binding: MutableStateFlow<ResultState<ImageBitmap?>>?,
        productVariantId: UUID,
        skipLoading: Boolean = false,
        onSuccessCallback: ((ImageBitmap?) -> Unit)? = null,
        ) {
        performRepositoryAction(
            binding = binding,
            skipLoading = skipLoading,
            action = { productVariantRepository.getProductVariantPicture(productVariantId) },
            onSuccess = { onSuccessCallback?.invoke(it) }
        )
    }

    fun fetchProductVariantListItemPicture(productVariantId: UUID) {
        val productVariantPicturesMap = _productUiState.value.productVariantImages.value
        if (!productVariantPicturesMap.containsKey(productVariantId)) {
            productVariantPicturesMap[productVariantId] = MutableStateFlow(ResultState.Idle)
        }
        productVariantPicturesMap[productVariantId]?.let {
            fetchProductVariantPicture(
                binding = it,
                productVariantId = productVariantId
            )
        }
    }

    fun uploadProductVariantImage(productVariantId: UUID, image: Uri) {
        performRepositoryAction(
            binding = null,
            action = {
                val imageMultiPartBodyPart = image.toMultipartBodyPart(context)
                productVariantRepository.uploadProductVariantPicture(productVariantId, imageMultiPartBodyPart)
            },
            onSuccess = {
                _productUiState.value.filteredByProduct?.let {
                    fetchProductVariantsList(it.productId, true)
                }
            }
        )
    }

    fun updateProductVariantImageStateIdle() {
        _productUiState.update { it.copy(
            productVariantImageState = MutableStateFlow(ResultState.Idle)
        ) }
    }

    fun deleteProductVariant(
        productVariantId: UUID,
        onSuccessCallback: (() -> Unit)? = null,
    ) {
        performRepositoryAction(
            binding = null,
            action = {
                productVariantRepository.deactivateProductVariant(productVariantId)
            },
            onSuccess = {
                _productUiState.value.filteredByProduct?.let {
                    fetchProductVariantsList(it.productId, true)
                }
                onSuccessCallback?.invoke()
            }
        )
    }

    fun saveCurrentAddEditProductVariant(productVariantCreateRequest: ProductVariantCreateRequest) {
        _productUiState.update {
            it.copy(
                productVariantCreateRequest = productVariantCreateRequest
            )
        }
    }

    fun updateCurrentAddEditProductVariant(
        productVariantId: UUID?,
        productId: UUID?,
    ) {
        if (productId == null || productVariantId == null) {
            currentAddEditProductVariantDetailsSetSuccessState(null)
        }
        if(productVariantId == null) {
            saveCurrentAddEditProductVariant(ProductVariantCreateRequest(productID = productId))
        }
        else {
            fetchProductVariantDetails(productVariantId) {
                saveCurrentAddEditProductVariant(
                    ProductVariantCreateRequest(productVariant = it, productID = productId)
                )
            }
            fetchProductVariantPicture(
                binding = _productUiState.value.productVariantImageState,
                productVariantId = productVariantId,
            )
        }
    }

    fun currentAddEditProductVariantDetailsSetSuccessState(productDetails: ProductVariantDetails?) {
        _productUiState.update { it.copy(
            productVariantDetailsState =
            MutableStateFlow(ResultState.Success(productDetails))
        ) }
    }

    fun updateCurrentProductVariantToDelete(productVariantSummary: ProductVariantSummary?) {
        _productUiState.update { it.copy(currentProductVariantToDelete = productVariantSummary) }
    }

    // -------------------- SIMPLE PRODUCT --------------------
    fun createSimpleProduct(
        product: ProductCreateRequest,
        productVariant: ProductVariantCreateRequest,
        onSuccessCallback: ((ProductDetails, ProductVariantDetails) -> Unit)? = null,
    ) {
        createProduct(product) { productDetails ->
            createProductVariant(productVariant.copy(
                productID = productDetails.productId)
            ) { productVariantDetails ->
                setProductMainVariant(
                    productDetails.productId,
                    productVariantDetails.productVariantId
                )
                onSuccessCallback?.invoke(productDetails, productVariantDetails)
            }
        }
    }

    fun saveSimpleProduct(
        simpleProduct: Pair<ProductCreateRequest, ProductVariantCreateRequest>?
    ) {
        var simpleProductToSave = simpleProduct
        if (simpleProductToSave == null) {
            simpleProductToSave = Pair(ProductCreateRequest(), ProductVariantCreateRequest())
        }
        _productUiState.update {
            it.copy(
                currentAddEditSimpleProduct = simpleProductToSave
            )
        }
    }


    // -------------------- FILTERS--------------------
    private fun applyFilters() {
        _productUiState.update { currState ->
            currState.copy(
                filteredSortedProductsList = if (currState.showProductsList) {
                    productFilter.filterProducts(
                        templates = currState.productsList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedProductsList,

                filteredSortedProductVariantsList = if (currState.showProductVariantsList) {
                    productFilter.filterProductVariants(
                        variants = currState.productVariantsList,
                        searchQuery = currState.searchQuery,
                        filterParams = currState.filterParams
                    )
                } else currState.filteredSortedProductVariantsList
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _productUiState.update { currState ->
            currState.copy(
                searchQuery = query,
            )
        }
        applyFilters()
    }

    fun showProductVariants(selectedProduct: ProductSummary?) {
        _productUiState.update { currState ->
            currState.copy(
                showProductsList = selectedProduct == null,
                showProductVariantsList = selectedProduct != null,
                filteredByProduct = selectedProduct
            )
        }
        applyFilters()
    }

    // Update filter state with one function call, reducing the need for multiple function parameters
    fun updateFilterParams(filterParams: FilterParams) {
        _productUiState.update { it.copy(
            filterParams = filterParams,
            filterParamsInEdit = filterParams
        ) }
        applyFilters()
    }

    fun saveFilterParamsInEdit(filterParams: FilterParams) {
        _productUiState.update { it.copy(filterParamsInEdit = filterParams) }
    }

    fun cancelFilters() {
        _productUiState.update { it.copy(filterParams = FilterParams()) }
        applyFilters()
    }

    fun clearCategoryFilter() {
        _productUiState.update { it.copy(filterParams = it.filterParams.copy(category = null)) }
        applyFilters()
    }

    fun clearPriceFilter(from: Boolean) {
        _productUiState.update { currState ->
            currState.copy(
                filterParams = currState.filterParams.copy(
                    priceFrom = if (from) null else currState.filterParams.priceFrom,
                    priceTo = if (!from) null else currState.filterParams.priceTo
                ),
            )
        }
        applyFilters()
    }

    // -------------------- UI STATE --------------------

    fun getCurrency(): Flow<String?> {
        return appPreferences.currency
    }

    fun expandBottomSheet(type: BottomSheetType, expand: Boolean) {
        _productUiState.update { currState ->
            currState.copy(
                menuBottomSheetExpanded = if (type == BottomSheetType.MENU) expand else currState.menuBottomSheetExpanded,
                addEditSimpleProductBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_SIMPLE) expand else currState.addEditSimpleProductBottomSheetExpanded,
                addEditProductVariantBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_VARIANT) expand else currState.addEditProductVariantBottomSheetExpanded,
                addEditProductBottomSheetExpanded = if (type == BottomSheetType.ADD_EDIT_PRODUCT) expand else currState.addEditProductBottomSheetExpanded,
                moreFiltersBottomSheetExpanded = if (type == BottomSheetType.MORE_FILTERS) expand else currState.moreFiltersBottomSheetExpanded,
                deleteProductBottomSheetExpanded = if (type == BottomSheetType.DELETE_PRODUCT) expand else currState.deleteProductBottomSheetExpanded
            )
        }
    }

    enum class BottomSheetType {
        MENU, ADD_EDIT_SIMPLE, ADD_EDIT_VARIANT, ADD_EDIT_PRODUCT, MORE_FILTERS, DELETE_PRODUCT
    }

    enum class SortingType {
        NONE, ASCENDING_NAME, DESCENDING_NAME, ASCENDING_PRICE, DESCENDING_PRICE
    }

    data class FilterParams(
        val sortingType: SortingType = SortingType.NONE,
        val priceFrom: BigDecimal? = null,
        val priceTo: BigDecimal? = null,
        val category: CategoryTree? = null
    )
}


