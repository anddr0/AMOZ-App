package com.example.amoz.ui.screens.entry

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.amoz.R
import com.example.amoz.api.enums.ImagePlaceholder
import com.example.amoz.api.enums.Sex
import com.example.amoz.api.requests.ContactPersonCreateRequest
import com.example.amoz.api.requests.PersonCreateRequest
import com.example.amoz.ui.components.ImageWithIcon
import com.example.amoz.ui.components.PrimaryFilledButton
import com.example.amoz.ui.screens.Screens
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun RegisterImageScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    userViewModel: UserViewModel = hiltViewModel(),
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.entry_pick_image),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))

                ImageWithIcon(
                    image = selectedImageUri?.toString(),
                    size = 300.dp,
                    iconImage = Icons.Outlined.Edit,
                    onImagePicked = {
                        selectedImageUri = it
                    },
                    placeholder = ImagePlaceholder.HUMAN,
                )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryFilledButton(
                onClick = {
                    if (selectedImageUri != null) {
                        userViewModel.updateCurrentUserImageUri(selectedImageUri!!)
                        userViewModel.registerUser( navController )
                    } else {
                        userViewModel.registerUser( navController )
                    }
                },
                text = stringResource(R.string.entry_create_an_account),
            )

        }
    }
}