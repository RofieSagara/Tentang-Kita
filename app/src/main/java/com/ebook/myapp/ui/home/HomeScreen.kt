package com.ebook.myapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.ebook.myapp.component.ItemBanner
import com.ebook.myapp.component.ItemModulePack
import com.ebook.myapp.component.ItemQuiz
import com.ebook.myapp.provide.LocalNavigationController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val navigation = LocalNavigationController.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is HomeScreenViewModel.Event.MoveToModule -> {
                    navigation.navigate("module?pack=${event.packID}")
                }
                is HomeScreenViewModel.Event.MoveToReader -> {
                    navigation.navigate("reader?path=${event.path}")
                }
                is HomeScreenViewModel.Event.MoveToQuiz -> {
                    navigation.navigate("quiz")
                }
            }
        }
    }

    HomeScreenContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeScreenViewModel.State,
    onEvent: (HomeScreenViewModel.UiEvent)->Unit
) {
    val headBanner = rememberAsyncImagePainter(model = state.headBanner)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "My Book Tentang Kita") },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF2CD3C9), titleContentColor = Color.White),
                actions = {
                    IconButton(onClick = { onEvent.invoke(HomeScreenViewModel.UiEvent.LogOut) }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF2CD3C9))
                .padding(paddingValues)
                .padding(14.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier
                        .height(88.dp)
                        .aspectRatio(298f / 88f),
                    painter = headBanner,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Free Test", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
                Spacer(modifier = Modifier.weight(1f, true))
                Text(
                    modifier = Modifier.clickable(
                        role = Role.Button,
                        onClick = { onEvent.invoke(HomeScreenViewModel.UiEvent.MoveToQuiz) }),
                    text = "View All>",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(state.quizCollection) {
                    ItemQuiz()
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Tentang Kita", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.modulePackCollection) {
                    key(it.id) {
                        ItemModulePack(
                            modifier = Modifier.fillMaxWidth(),
                            title = it.title,
                            description = it.description,
                            image = it.photo,
                            background = it.background,
                            onClick = {
                                onEvent.invoke(HomeScreenViewModel.UiEvent.MoveToModule(it))
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                items(state.bannerCollection) {
                    key(it.id) {
                        ItemBanner(
                            image = it.photo,
                            onClick = {
                                onEvent.invoke(HomeScreenViewModel.UiEvent.MoveToReader(it))
                            }
                        )
                    }
                }
            }
        }
    }
}