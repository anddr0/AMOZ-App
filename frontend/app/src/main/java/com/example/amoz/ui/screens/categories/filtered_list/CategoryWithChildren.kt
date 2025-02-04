package com.example.amoz.ui.screens.categories.filtered_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.amoz.models.CategoryTree

@Composable
fun CategoryWithChildren(
    category: CategoryTree,
    allCategories: List<CategoryTree>,
    level: Int = 0,
    isSelectable: Boolean,
    isSelectableLeavesOnly: Boolean,
    isChild: Boolean = false,
    onClick: ((CategoryTree) -> Unit)? = null,
    onDelete: ((CategoryTree) -> Unit)? = null,
) {
    val children = category.childCategories
    var childrenListExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CategoryListItem(
            category = category,
            isChild = isChild,
            isExpanded = childrenListExpanded,
            hasChildren = children.isNotEmpty(),
            onExpand = { childrenListExpanded = !childrenListExpanded },
            onClick = onClick,
            onDelete = onDelete,
            isSelectable = isSelectable,
            isSelectableLeavesOnly = isSelectableLeavesOnly,
        )

        if (childrenListExpanded) {
            children.forEach { child ->
                Box(
                    modifier = Modifier.padding(start = (5.dp * (level + 1)))
                ) {
                    CategoryWithChildren(
                        category = child,
                        allCategories = allCategories,
                        level = level + 1,
                        isChild = true,
                        onClick = onClick,
                        onDelete = onDelete,
                        isSelectable = isSelectable,
                        isSelectableLeavesOnly = isSelectableLeavesOnly,
                    )
                }
            }
        }
    }
}