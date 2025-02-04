package com.example.amoz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.amoz.models.Invitation


@Composable
fun CompanyInvitationListItem(
    invitation: Invitation,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = invitation.company.name,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Text(
                text = "Owner: ${invitation.sender.person.name} ${invitation.sender.person.surname}",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onAcceptClick) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept Invitation",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = onDeclineClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Decline Invitation",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    )
}
