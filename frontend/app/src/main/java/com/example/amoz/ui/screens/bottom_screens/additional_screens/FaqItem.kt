package com.example.amoz.ui.screens.bottom_screens.additional_screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FaqItem(question: String, answer: String) {
    var faqItemExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, SolidColor(MaterialTheme.colorScheme.outline), RoundedCornerShape(10.dp))
    ) {
        ListItem(
            modifier = Modifier
                .clickable { faqItemExpanded = !faqItemExpanded },
            leadingContent = { Icon(Icons.Default.QuestionMark, null) },
            headlineContent = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = question,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(5.dp))
                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector =
                        if (faqItemExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "show product stock",
                    )
                }

            },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        )

        if (faqItemExpanded) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = answer,
            )
        }
    }
}