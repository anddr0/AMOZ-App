package com.example.amoz.ui.components.text_fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    placeholder: String,
    isMoreFiltersVisible: Boolean = true,
    onSearchQueryChange: (String) -> Unit,
    onMoreFiltersClick: () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { onSearchQueryChange(it) },
        placeholder = { Text(text = placeholder) },

        singleLine = true,
        maxLines = 1,
        trailingIcon = {
            Row {
                AnimatedVisibility(
                    visible = searchQuery.isNotEmpty(),
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300))
                ) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Clear Search")
                    }
                }
                if(isMoreFiltersVisible){
                    IconButton(onClick = onMoreFiltersClick) {
                        Icon(imageVector = Icons.Filled.FilterAlt, contentDescription = "More filters")
                    }
                }
            }
        },
        shape = RoundedCornerShape(15.dp)
    )
}