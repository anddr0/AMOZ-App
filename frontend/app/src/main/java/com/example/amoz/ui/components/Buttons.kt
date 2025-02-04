package com.example.amoz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.R

@Composable
fun PrimaryFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingIconDescription
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrimaryOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingIconDescription
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrimaryOutlinedButton(
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector? = null,
    leadingIconDescription: String? = null,
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingIconDescription
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CloseOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 4.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.google_icon),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.entry_sign_in_with_google),
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun MoreOrdersTextButton(onClick: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.home_last_orders))
        Text(stringResource(R.string.more),
            modifier = Modifier.clickable {
                onClick()
            },
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )
    }
}