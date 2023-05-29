package com.ebook.myapp.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import okhttp3.internal.wait

@Composable
fun ItemQuiz(modifier: Modifier = Modifier) {
    IconButton(modifier = modifier, onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White)
    }
}