package com.ebook.myapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ebook.myapp.ui.login.LoginScreenContent
import com.ebook.myapp.ui.login.LoginScreenViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun showLoginState() {
        val state = LoginScreenViewModel.State.Login(isLoading = false, errorMessage = "")

        rule.setContent { LoginScreenContent(state = state, onEvent = { }) }
        rule.onNodeWithText("Already Have Account?").assertExists()
        rule.onNodeWithText("Login").assertExists()
        rule.onNodeWithText("New user? Register Now").assertExists()
        rule.onNodeWithText("Email").assertExists()
        rule.onNodeWithText("Password").assertExists()
    }

    @Test
    fun showRegisterState() {
        val state = LoginScreenViewModel.State.Register(isLoading = false, errorMessage = "")

        rule.setContent { LoginScreenContent(state = state, onEvent = { }) }
        rule.onNodeWithText("Here's your first step with us!").assertExists()
        rule.onNodeWithText("Register").assertExists()
        rule.onNodeWithText("Email").assertExists()
        rule.onNodeWithText("Password").assertExists()
        rule.onNodeWithText("Re-Password").assertExists()
    }
}