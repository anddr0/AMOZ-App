package com.example.amoz.ui.states

import com.example.amoz.api.requests.CategoryCreateRequest
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.models.CategoryTree
import kotlinx.coroutines.flow.MutableStateFlow

data class CategoriesUiState (
    val categoriesListFetched: MutableStateFlow<ResultState<List<CategoryTree>>> = MutableStateFlow(ResultState.Idle),

    val categoryList: List<CategoryTree> = emptyList(),
    val filteredCategoryList: List<CategoryTree> = categoryList,

    val searchQuery: String = "",

    val currentCategoryTree: CategoryTree? = null,
    val currentCategoryCreateRequest: CategoryCreateRequest? = null,

    val addEditCategoryBottomSheetExpanded: Boolean = false,
    val deleteCategoryConfirmBottomSheetExpanded: Boolean = false,
)