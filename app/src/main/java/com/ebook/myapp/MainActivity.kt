package com.ebook.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ebook.myapp.provide.LocalNavigationController
import com.ebook.myapp.ui.home.HomeScreen
import com.ebook.myapp.ui.login.LoginScreen
import com.ebook.myapp.ui.module.ModuleScreen
import com.ebook.myapp.ui.quiz.QuizScreen
import com.ebook.myapp.ui.reader.ReaderScreen
import com.ebook.myapp.ui.theme.TentangKitaTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TentangKitaTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigation = rememberNavController()

                    CompositionLocalProvider(LocalNavigationController provides navigation) {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navigation = LocalNavigationController.current
    val userID by viewModel.userUID.collectAsState()

    LaunchedEffect(userID) {
        if (Firebase.auth.currentUser == null) {
            navigation.navigate("login") {
                popUpTo("home") {
                    inclusive = true
                }
            }
        }
    }

    NavHost(navController = navigation, startDestination = "home") {
        composable(route = "home") {
            HomeScreen()
        }

        composable(route = "login") {
            LoginScreen()
        }

        composable(
            route = "module?pack={pack}",
            arguments = listOf(navArgument("pack") { defaultValue = "" })
        ) {
            val pack = it.arguments?.getString("pack") ?: ""
            if (pack.isBlank()) {
                navigation.navigateUp()
                return@composable
            }

            ModuleScreen(packID = pack)
        }

        composable(
            route = "reader?path={path}",
            arguments = listOf(navArgument("path") { defaultValue = "" })
        ) {
            val path = it.arguments?.getString("path") ?: ""
            if (path.isBlank()) {
                navigation.navigateUp()
                return@composable
            }

            ReaderScreen(path = path)
        }

        composable(route = "quiz") {
            QuizScreen()
        }
    }
}