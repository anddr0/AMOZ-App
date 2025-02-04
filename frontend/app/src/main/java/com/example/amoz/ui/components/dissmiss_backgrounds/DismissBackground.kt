package com.example.amoz.ui.components.dissmiss_backgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.amoz.ui.theme.customColor2Dark
import com.example.amoz.ui.theme.customColor3Dark
import com.example.amoz.ui.theme.onCustomColor2Dark
import com.example.amoz.ui.theme.onCustomColor3Dark

@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState, endToStart: Boolean = false) {
    val color = when {
        dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd -> Color(customColor2Dark.value)
        dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart && endToStart -> Color(customColor3Dark.value)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            tint = Color(onCustomColor2Dark.value),
            contentDescription = "Remove product template"
        )
        Spacer(modifier = Modifier)
        if (endToStart) {
            Icon(
                imageVector = Icons.Default.Edit,
                tint = Color(onCustomColor3Dark.value),
                contentDescription = "Edit product template"
            )
        }
    }
}