package com.example.amoz.ui.screens.categories.filtered_list

import com.example.amoz.models.CategoryTree
import java.util.UUID

class CategoryListFilter {

    fun filterCategoryList(
        categoryList: List<CategoryTree>,
        searchQuery: String
    ): List<CategoryTree> {
        if (searchQuery.isBlank()) {
            return categoryList.filter { it.categoryLevel.toInt() == 1 }.sortedBy { it.name }
        }

        val resultSet = mutableSetOf<CategoryTree>()

        val filteredList = categoryList.filter { category ->
            category.name.contains(searchQuery, ignoreCase = true)
        }
        resultSet.addAll(filteredList)

        val filteredChildren = categoryList.flatMap { category ->
            filterCategoryList(category.childCategories, searchQuery)
        }
        resultSet.addAll(filteredChildren)

        return resultSet.toList().sortedBy { it.name }
    }

    fun findParentId(
        roots: List<CategoryTree>,
        childId: UUID
    ): UUID? {
        for (root in roots) {
            val result = findParentId(root, childId)
            if (result != null) {
                return result
            }
        }
        return null
    }

    private fun findParentId(
        root: CategoryTree,
        childId: UUID,
        parent: CategoryTree? = null
    ): UUID? {
        if (root.categoryId == childId) {
            return parent?.categoryId
        }

        for (child in root.childCategories) {
            val result = findParentId(child, childId, root)
            if (result != null) {
                return result
            }
        }
        return null
    }
}
