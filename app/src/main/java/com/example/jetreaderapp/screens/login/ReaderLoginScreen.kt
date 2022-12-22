package com.example.jetreaderapp.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetreaderapp.R
import com.example.jetreaderapp.components.EmailInput
import com.example.jetreaderapp.components.PasswordInput
import com.example.jetreaderapp.components.ReaderLogo
import com.example.jetreaderapp.navigation.ReaderScreens

@Preview
@Composable
fun ReaderLoginScreen(navController: NavHostController = NavHostController(LocalContext.current),
viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    val function = {
        navController.navigate(ReaderScreens.ReaderHomeScreen.name) {
            popUpTo(ReaderScreens.LoginScreen.name) {
                inclusive = true
            }
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ReaderLogo()
            UserForm(isCreateAccount = !showLoginForm.value, loading = false) {email, pass->
                Log.d("Login", "Login Values: $email, $pass")
                if(showLoginForm.value) {
                    viewModel.singInWithEmailAndPassword(email, pass) {
                        function()
                    }
                } else {
                    viewModel.createUserWithEmailAndPassword(email, pass) {
                        function()
                    }
                }

            }

            Spacer(modifier = Modifier.height(15.dp))

            val textSignupLogin = if(showLoginForm.value) "Sign up" else "Login"
            val textNewUser = if(showLoginForm.value) "New User?" else "Already have an account?"
            Row {
                Text(textNewUser)
                Text(
                    textSignupLogin,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
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
        //.height(250.dp)
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        //.verticalScroll(rememberScrollState())

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if(isCreateAccount) Text(stringResource(R.string.password_suggestion),
            modifier = Modifier.padding(4.dp)) else Box{}
        EmailInput(modifier = Modifier.fillMaxWidth(),
            emailState = email,
            onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })
        PasswordInput(modifier = Modifier
            .focusRequester(passwordFocusRequest)
            .fillMaxWidth(),
            passwordState = password,
            labelId = "Password",
            passwordVisibility = passwordVisibility,
            loading = !loading,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value)
                keyboardController?.hide()
            })

        SubmitButton(
            textId = if(isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,
        ) {
            onDone(email.value.trim(), password.value)
            keyboardController?.hide()
        }
    }

}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInputs: Boolean,
onClick : ()-> Unit = {}) {
    Button(onClick = onClick,
    modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(),
    enabled = !loading && validInputs,
        shape = CircleShape,
    ){
        if(loading) CircularProgressIndicator(Modifier.size(25.dp))
        else Text(textId, modifier = Modifier.padding(5.dp))
    }
}


