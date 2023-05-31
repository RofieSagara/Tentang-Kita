package com.ebook.myapp.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizzi.bouquet.VerticalPDFReader
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebook.myapp.provide.LocalNavigationController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReaderScreen(viewModel: ReaderScreenViewModel = hiltViewModel(), path: String) {
    val state by viewModel.state.collectAsState()
    val navigation = LocalNavigationController.current

    LaunchedEffect(Unit) {
        viewModel.set(path)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is ReaderScreenViewModel.Event.MoveBack -> {
                    navigation.navigateUp()
                }
            }
        }
    }

    ReaderScreenContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreenContent(
    state: ReaderScreenViewModel.State,
    onEvent: (ReaderScreenViewModel.UiEvent)->Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { }, navigationIcon = {
                IconButton(onClick = { onEvent.invoke(ReaderScreenViewModel.UiEvent.MoveBack) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            state.pdfState?.let { verticalPdfReaderState ->
                VerticalPDFReader(
                    state = verticalPdfReaderState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Gray)
                )
            }
        }
    }
}