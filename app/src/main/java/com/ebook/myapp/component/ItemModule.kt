package com.ebook.myapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ItemModule(
    modifier: Modifier = Modifier,
    image: String,
    onClick: ()->Unit
) {
    val imageCover = rememberAsyncImagePainter(model = image)
    val onClickEvent by rememberUpdatedState(onClick)

    Card(
        modifier = modifier
            .height(100.dp)
            .aspectRatio(150 / 100f)
            .clickable(role = Role.Button, onClick = onClickEvent)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = imageCover,
            contentDescription = null
        )
    }
}