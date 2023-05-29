package com.ebook.myapp.provide

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController

val LocalNavigationController = staticCompositionLocalOf<NavHostController> { error("LocalNavigationController not implement") }