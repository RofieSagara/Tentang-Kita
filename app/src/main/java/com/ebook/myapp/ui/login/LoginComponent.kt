package com.ebook.myapp.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ebook.myapp.ui.theme.TentangKitaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String)->Unit,
    password: String,
    onPasswordChange: (String)->Unit,
    onLogin: ()->Unit,
    onRegister: ()->Unit
) {
    val emailChange by rememberUpdatedState(onEmailChange)
    val passwordChange by rememberUpdatedState(onPasswordChange)
    val login by rememberUpdatedState(onLogin)
    val register by rememberUpdatedState(onRegister)

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = emailChange,
            label = {
                Text(text = "Email")
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = passwordChange,
            label = {
                Text(text = "Password")
            },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(modifier = Modifier.fillMaxWidth(), onClick = login) {
            Text(text = "Login")
        }
        Text(
            modifier = Modifier.clickable(role = Role.Button, onClick = register),
            text = "New user? Register Now",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    onRegister: (String, String)->Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    val isValid = remember(password, rePassword) {
        if (password.isBlank() || rePassword.isBlank()) return@remember false
        return@remember password == rePassword
    }
    val register by rememberUpdatedState(onRegister)

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = {
                Text(text = "Email")
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = {
                Text(text = "Password")
            },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = rePassword,
            onValueChange = { rePassword = it },
            label = {
                Text(text = "Re-Password")
            },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isValid,
            onClick = { register.invoke(email, password) }
        ) {
            Text(text = "Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    TentangKitaTheme(darkTheme = false, dynamicColor = false) {
        LoginForm(
            modifier = Modifier.fillMaxWidth(),
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            onLogin = { },
            onRegister = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFormPreview() {
    TentangKitaTheme(darkTheme = false, dynamicColor = false) {
        RegisterForm(onRegister = { _, _ -> })
    }
}