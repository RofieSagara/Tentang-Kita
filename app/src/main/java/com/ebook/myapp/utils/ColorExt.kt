package com.ebook.myapp.utils

import androidx.compose.ui.graphics.Color

fun Color.Companion.fromHex(color: String) = Color(android.graphics.Color.parseColor("#${color.replace("#", "")}"))