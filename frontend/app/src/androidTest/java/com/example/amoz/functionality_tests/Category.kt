package com.example.amoz.functionality_tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.amoz.api.repositories.CategoryRepository
import com.example.amoz.api.requests.CategoryCreateRequest
import com.example.amoz.api.requests.field_names.CategoryFieldNames
import com.example.amoz.app.SignOutManager
import com.example.amoz.models.CategoryTree
import com.example.amoz.view_models.CategoriesViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class Category {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var categoryRepository: CategoryRepository
    @Inject
    lateinit var signOutManager: SignOutManager
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var testCategoriesIdList: MutableList<UUID>

    @Before
    fun setup() {
        hiltRule.inject()
        categoriesViewModel = CategoriesViewModel(categoryRepository, signOutManager)
        testCategoriesIdList = mutableListOf()
    }

    @Test
    fun createValidCategory() {
        val categoryToCreate = getSimpleCategory("Simple Category")

        categoriesViewModel.createCategory(categoryToCreate) { categoryDetails ->
            val categoryId = categoryDetails.categoryId
            addCategoryIdToDeleteList(categoryId) // Save id to delete later

            assertCategoryInList(categoryId) { assert(it) }
        }
    }

    @Test
    fun createCategoryInvalidName() {
        val categoryToCreate = getSimpleCategory("")

        try { categoriesViewModel.createCategory(categoryToCreate) }
        catch (e: IllegalArgumentException) {
            val errorMessage = "${CategoryFieldNames.CATEGORY_NAME} should not be blank"

            assertEquals(errorMessage, e.message)
        }
    }

    @Test
    fun createSubcategory() {
        val simpleParentCategory = getSimpleCategory("Simple Category")

        categoriesViewModel.createCategory(simpleParentCategory) { parentCategoryDetails ->
            val parentCategoryId = parentCategoryDetails.categoryId
            val simpleSubcategory = getSimpleCategory(
                name = "Simple Subcategory",
                parentCategoryId = parentCategoryId
            )
            addCategoryIdToDeleteList(parentCategoryId)

            categoriesViewModel.createCategory(simpleSubcategory) { subcategoryDetails ->
                val subcategoryId = subcategoryDetails.categoryId

                assertCategoryInList(parentCategoryId) { assert(it) }
                assertCategoryInList(subcategoryId) { assert(it) }
            }
        }
    }

    @Test
    fun updateCategory() {
        val simpleParentCategory = getSimpleCategory("Simple Category")

        categoriesViewModel.createCategory(simpleParentCategory) { parentCategoryDetails ->
            val simpleCategoryId = parentCategoryDetails.categoryId
            val updatedCategory = getSimpleCategory(
                name = "Updated category",
            )
            addCategoryIdToDeleteList(simpleCategoryId)

            categoriesViewModel.updateCategory(simpleCategoryId, updatedCategory) {
                categoriesViewModel.fetchCategories { list ->
                    val foundSimpleCategory = list.find { it.categoryId == simpleCategoryId }

                    assertEquals("Updated category", foundSimpleCategory?.name)
                }
            }
        }
    }

    @Test
    fun deleteCategory() {
        val simpleParentCategory = getSimpleCategory("Simple Category")
        categoriesViewModel.createCategory(simpleParentCategory) { categoryDetails ->
            val categoryId = categoryDetails.categoryId
            categoriesViewModel.deleteCategory(categoryId) {
                assertCategoryInList(categoryId) { assert(!it) }
            }
        }
    }

    @Test
    fun deleteCategoryWithSubcategories() {
        val simpleParentCategory = getSimpleCategory("Simple Category")

        categoriesViewModel.createCategory(simpleParentCategory) { parentCategoryDetails ->
            val parentCategoryId = parentCategoryDetails.categoryId
            val simpleSubcategory = getSimpleCategory(
                name = "Simple Subcategory",
                parentCategoryId = parentCategoryId
            )
            categoriesViewModel.createCategory(simpleSubcategory) { subcategoryDetails ->
                val subcategoryId = subcategoryDetails.categoryId
                assertCategoryInList(subcategoryId) { assert(it) }

                categoriesViewModel.deleteCategory(parentCategoryId) {
                    assertCategoryInList(parentCategoryId) { assert(!it) }
                    assertCategoryInList(subcategoryId) { assert(!it) }
                }
            }
        }
    }


    @After
    fun clearTestData() {
        testCategoriesIdList.forEach {
            categoriesViewModel.deleteCategory(it)
        }
    }

    private fun assertCategoryInList(categoryId: UUID, isFound: (Boolean) -> Unit) {
        categoriesViewModel.fetchCategories(
            onSuccessCallback = { categoriesList ->
                val foundCategory = findCategory(categoriesList, categoryId)
                isFound(foundCategory)
            }
        )
    }

    private fun findCategory(categoriesList: List<CategoryTree>, categoryId: UUID): Boolean {
        for (category in categoriesList) {
            if (category.categoryId == categoryId) {
                return true
            }
            if (findCategory(category.childCategories, categoryId)) {
                return true
            }
        }
        return false
    }


    private fun getSimpleCategory(
        name: String="", parentCategoryId: UUID?=null
    ): CategoryCreateRequest {
        return CategoryCreateRequest(name, parentCategoryId)
    }

    private fun addCategoryIdToDeleteList(categoryId: UUID) {
        testCategoriesIdList.add(categoryId)
    }

}