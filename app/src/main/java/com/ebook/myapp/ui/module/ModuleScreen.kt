package com.ebook.myapp.ui.module

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ebook.myapp.R
import com.ebook.myapp.component.ItemModule
import com.ebook.myapp.ui.theme.TentangKitaTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ebook.myapp.provide.LocalNavigationController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ModuleScreen(viewModel: ModuleScreenViewModel = hiltViewModel(), packID: String) {
    val state by viewModel.state.collectAsState()
    val navigation = LocalNavigationController.current

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when(event) {
                is ModuleScreenViewModel.Event.MoveToReader -> {
                    navigation.navigate("reader?path=${event.path}")
                }
                is ModuleScreenViewModel.Event.MoveBack -> {
                    navigation.navigateUp()
                }
            }
        }
    }

    viewModel.set(packID = packID)

    ModuleScreenContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleScreenContent(
    state: ModuleScreenViewModel.State,
    onEvent: (ModuleScreenViewModel.UiEvent)->Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Image(modifier = Modifier.fillMaxWidth(), painter = painterResource(id = R.drawable.ellipse_module), contentDescription = null, contentScale = ContentScale.FillWidth)
            Column {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { onEvent.invoke(ModuleScreenViewModel.UiEvent.MoveBack) }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = state.modulePack?.title.orEmpty(), style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(28.dp))
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth(),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(state.moduleCollection) {
                            key(it.id) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ItemModule(
                                        image = it.photo,
                                        onClick = {
                                            onEvent.invoke(ModuleScreenViewModel.UiEvent.MoveToReader(it))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ModuleScreenContentPreview() {
    TentangKitaTheme(darkTheme = false, dynamicColor = false) {
        ModuleScreenContent(state = ModuleScreenViewModel.State(listOf(), null), onEvent = {  })
    }
}