package com.example.amoz.ui.screens.bottom_screens.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amoz.R
import com.example.amoz.api.sealed.ResultState
import com.example.amoz.ui.components.ResultStateView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CompanyLogoAndName(
    companyLogoState: MutableStateFlow<ResultState<ImageBitmap>>,
    companyName: String,
    readOnly: Boolean = false,
    onCompanyNameClick: () -> Unit,
    onCompanyImageClick: () -> Unit,
) {
    val imageModifier = Modifier
        .size(125.dp)
        .padding(16.dp)
        .clip(RoundedCornerShape(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ResultStateView(
            modifier = Modifier
                .clickable { onCompanyImageClick() },
            state = companyLogoState,
            failureView = if (!readOnly) {
                {
                    Box(
                        modifier = imageModifier.background(MaterialTheme.colorScheme.surfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, null)
                    }
                }} else null
        ) { image ->
            Image(
                modifier = imageModifier,
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Column {
            Row(
                modifier = Modifier.clickable { onCompanyNameClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(5.dp))
                if (!readOnly) {
                    Icon(
                        imageVector = Icons.Outlined.ModeEdit,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id =
                    if (!readOnly) R.string.company_screen_description_owner
                    else R.string.company_screen_description_regular),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}