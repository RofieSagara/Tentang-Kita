package com.ebook.myapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ebook.myapp.R
import com.ebook.myapp.provide.LocalNavigationController
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(viewModel: LoginScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var eventState by remember { mutableStateOf<LoginScreenViewModel.Event>(LoginScreenViewModel.Event.Idle) }
    val navigation = LocalNavigationController.current

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when(event) {
                is LoginScreenViewModel.Event.Error -> {
                    eventState = event
                }
                is LoginScreenViewModel.Event.Idle -> {}
                is LoginScreenViewModel.Event.MoveToHome -> {
                    navigation.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    LoginScreenContent(state = state, event = eventState, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    state: LoginScreenViewModel.State,
    event: LoginScreenViewModel.Event?,
    onEvent: (LoginScreenViewModel.UiEvent)->Unit
) {
    val eventLaunch by rememberUpdatedState(onEvent)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val title = remember(state) {
        return@remember if (state.isLogin)
            "Already Have Account?" else
            "Here's your first step with us!"
    }
    val icon = remember(state.isLogin) {
        return@remember if (state.isLogin)
            R.drawable.ellipse_login else
            R.drawable.ellipse_login

    }
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    LaunchedEffect(event) {
        when(event) {
            is LoginScreenViewModel.Event.Error -> {
                snackbarHostState.showSnackbar(message = event.message)
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.widthIn(max = 200.dp), text = title, style = MaterialTheme.typography.headlineLarge)
                Image(modifier = Modifier.width(200.dp), painter = painterResource(id = icon), contentDescription = null, contentScale = ContentScale.FillWidth)
            }

            if (state.isLogin) {
                LoginForm(
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    onLogin = {
                        eventLaunch.invoke(LoginScreenViewModel.UiEvent.LoginUser(email, password))
                    },
                    onRegister = {
                        eventLaunch.invoke(LoginScreenViewModel.UiEvent.MoveToRegister)
                    }
                )
            } else {
                RegisterForm(
                    onRegister = { email, password ->
                        eventLaunch.invoke(LoginScreenViewModel.UiEvent.RegisterUser(email, password))
                    }
                )
            }
        }
    }
}