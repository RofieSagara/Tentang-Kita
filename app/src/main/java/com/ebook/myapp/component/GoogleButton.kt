package com.ebook.myapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebook.myapp.R

@Composable
fun GoogleButton(modifier: Modifier = Modifier, text: String = "Sign in with Google", onClick: ()->Unit) {
    Row(
        modifier = modifier
            .background(color = Color(0xFF4285F4))
            .height(40.dp)
            .clickable { onClick.invoke() }
            .padding(1.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f / 1f)
            .background(color = Color.White), contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier
                    .size(18.dp),
                painter = painterResource(id = R.drawable.icons_google),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = text, style = MaterialTheme.typography.labelLarge.copy(color = Color.White))
        Spacer(modifier = Modifier.width(8.dp))
    }
}