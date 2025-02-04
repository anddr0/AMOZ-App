package com.example.amoz.view_models

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.amoz.api.repositories.CategoryRepository
import com.example.amoz.api.requests.AddressCreateRequest
import com.example.amoz.api.requests.CategoryCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.app.SignOutManager
import com.example.amoz.models.CategoryDetails
import com.example.amoz.models.CategoryTree
import com.example.amoz.ui.states.CategoriesUiState
import com.example.amoz.ui.screens.categories.filtered_list.CategoryListFilter
import com.example.amoz.ui.states.OrderUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val signOutManager: SignOutManager,
): BaseViewModel() {
    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _categoriesUiState.asStateFlow()
    private val categoryListFilter = CategoryListFilter()

    init {
        fetchCategories()
        observeSignOutEvent()
    }

    private fun observeSignOutEvent() {
        viewModelScope.launch {
            signOutManager.signOutEvent.collect {
                clearState()
            }
        }
    }

    private fun clearState() {
        _categoriesUiState.update { CategoriesUiState() }
    }

    fun fetchCategories(
        binding: MutableStateFlow<ResultState<List<CategoryTree>>>? =
            _categoriesUiState.value.categoriesListFetched,
        skipLoading: Boolean = false,
        onSuccessCallback: ((categoriesList: List<CategoryTree>) -> Unit)? = null
    ) {
        performRepositoryAction(
            binding = binding,
            failureMessage = "Could not fetch categories, try again",
            skipLoading = skipLoading,
            action = { categoryRepository.getAllCompanyCategories() },
            onSuccess = {
                updateCategoryUiStateList(it)
                onSuccessCallback?.invoke(it)
            }
        )
    }

    fun createCategory(
        categoryCreateRequest: CategoryCreateRequest,
        subcategories: List<String>? = null,
        onSuccessCallback: ((CategoryDetails) -> Unit)? = null
    ) {
        val validatorMessage = categoryCreateRequest.validate()
        if (validatorMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Could not add category, try later",
                action = { categoryRepository.createCategory(categoryCreateRequest) },
                onSuccess = { createdCategory ->
                    createCategorySubCategories(createdCategory.categoryId, subcategories)
                    fetchCategories(skipLoading = true)
                    onSuccessCallback?.invoke(createdCategory)
                }
            )
        }
        else {
            Log.e(tag, validatorMessage)
            throw IllegalArgumentException(validatorMessage)
        }
    }

    fun updateCategory(
        categoryId: UUID,
        categoryCreateRequest: CategoryCreateRequest,
        subcategories: List<String>? = null,
        onSuccessCallback: ((CategoryDetails) -> Unit)? = null
        ) {
        val validatorMessage = categoryCreateRequest.validate()
        if (validatorMessage == null) {
            performRepositoryAction(
                binding = null,
                failureMessage = "Could not update category, try later",
                action = { categoryRepository.updateCategory(categoryId, categoryCreateRequest) },
                onSuccess = { updatedCategory ->
                    createCategorySubCategories(updatedCategory.categoryId, subcategories)
                    fetchCategories(skipLoading = true)
                    onSuccessCallback?.invoke(updatedCategory)
                }
            )
        }
        else {
            Log.e(tag, validatorMessage)
            throw IllegalArgumentException(validatorMessage)
        }
    }

    fun deleteCategory(
        categoryId: UUID,
        onSuccessCallback: (() -> Unit)? = null
    ) {
        performRepositoryAction(
            binding = null,
            failureMessage = "Could not delete category, try later",
            action = { categoryRepository.deleteCategory(categoryId) },
            onSuccess = {
                fetchCategories(skipLoading = true)
                onSuccessCallback?.invoke()
            }
        )
    }

    fun createCategorySubCategories(
        parentCategoryId: UUID,
        subcategories: List<String>? = null
    ) {
        subcategories?.let { it.forEach { subcategory ->
                createCategory(CategoryCreateRequest(subcategory, parentCategoryId))
            }
        }
    }

    fun updateCurrentCategory(category: CategoryTree?) {
        _categoriesUiState.update { currState ->
            currState.copy(
                currentCategoryTree = category,
                currentCategoryCreateRequest = CategoryCreateRequest(
                    name = category?.name ?: "",
                    parentCategoryId = category?.categoryId?.let {
                        categoryListFilter.findParentId(
                            roots = currState.categoryList,
                            childId = it
                        )
                    }
                )
            )
        }
    }

    fun updateCategoryUiStateList(categoriesList: List<CategoryTree>) {
        _categoriesUiState.update { it.copy(categoryList = categoriesList) }
        applyFilters()
    }

    private fun applyFilters() {
        _categoriesUiState.update { currState ->
            currState.copy(
                filteredCategoryList = categoryListFilter.filterCategoryList(
                    categoryList = currState.categoryList,
                    searchQuery = currState.searchQuery
                )
            )
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        _categoriesUiState.update { it.copy(searchQuery = searchQuery) }
        applyFilters()
    }

    fun expandAddEditCategoryBottomSheet(expand: Boolean) {
        _categoriesUiState.update { currState ->
            currState.copy(
                addEditCategoryBottomSheetExpanded = expand
            )
        }
    }

    fun expandDeleteCategoryConfirmBottomSheet(expand: Boolean) {
        _categoriesUiState.update { currState ->
            currState.copy(
                deleteCategoryConfirmBottomSheetExpanded = expand
            )
        }
    }
}