package com.example.amoz.ui.components.pickers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R
import com.example.amoz.models.CategoryTree
import com.example.amoz.pickers.CategoryPicker
import java.util.UUID

@Composable
fun <T> CategoryPickerListItem(
    modifier: Modifier = Modifier,
    category: T?,
    leavesOnly: Boolean = false,
    navController: NavController,
    onSaveState: () -> Unit,
    onCategoryChange: (CategoryTree?) -> Unit,
    getCategoryId: (T?) -> UUID?,
    getCategoryName: (T?) -> String?,
) {
    val categoryPicker = CategoryPicker(navController)

    val selectedCategory = categoryPicker.getPickedCategory()

    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null && selectedCategory.categoryId != getCategoryId(category)) {
            onCategoryChange(selectedCategory)
            categoryPicker.removePickedCategory()
        }
    }

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onSaveState()
                categoryPicker.navigateToCategoryScreen(leavesOnly)
            },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Category,
                contentDescription = null
            )
        },
        overlineContent = { Text(stringResource(R.string.product_category)) },
        headlineContent = {
            Text(
                text = getCategoryName(category) ?: stringResource(R.string.product_category_choose)
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                category?.let {
                    IconButton(
                        onClick = {
                            onCategoryChange(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}

