package com.ebook.myapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ItemBanner(
    modifier: Modifier = Modifier,
    image: String,
    onClick: ()->Unit
) {
    val imageCover = rememberAsyncImagePainter(model = image)
    val onClickEvent by rememberUpdatedState(onClick)

    Card(modifier = modifier.clickable(role = Role.Button, onClick = onClickEvent)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(297/88f),
            painter = imageCover,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}
