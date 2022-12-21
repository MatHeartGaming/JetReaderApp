package com.example.jetreaderapp.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetreaderapp.components.EmailInput
import com.example.jetreaderapp.components.ReaderLogo

@Composable
fun ReaderLoginScreen(navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxWidth()) {
            ReaderLogo()
            UserForm()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun UserForm() {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.isNotBlank() && password.value.isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(emailState = email, enabled = true, onAction = KeyboardActions{
            passwordFocusRequest.requestFocus()
        })
    }

}