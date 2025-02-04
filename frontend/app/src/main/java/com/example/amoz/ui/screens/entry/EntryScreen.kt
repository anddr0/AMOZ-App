package com.example.amoz.ui.screens.entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.amoz.R
import com.example.amoz.ui.components.GoogleSignInButton
import com.example.amoz.ui.theme.AmozApplicationTheme

import com.example.amoz.view_models.UserViewModel


@Composable
fun EntryScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    userViewModel: UserViewModel,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = rememberAsyncImagePainter(R.drawable.app_logo),
                contentDescription = "App Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                text = stringResource(id = R.string.entry_app_description),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            )

            GoogleSignInButton {
                userViewModel.navigateUser(navController)
            }
        }
    }
}