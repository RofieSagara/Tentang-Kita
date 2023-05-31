package com.ebook.myapp.ui.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ebook.myapp.provide.LocalNavigationController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizScreen(viewModel: QuizScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val navigation = LocalNavigationController.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is QuizScreenViewModel.Event.OnBack -> {
                    navigation.navigateUp()
                }
            }
        }
    }

    QuizScreenContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreenContent(
    state: QuizScreenViewModel.State,
    onEvent: (QuizScreenViewModel.UiEvent)->Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Quiz") },
                navigationIcon = {
                    IconButton(onClick = { onEvent.invoke(QuizScreenViewModel.UiEvent.OnBack) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = { onEvent.invoke(QuizScreenViewModel.UiEvent.Next) }) {
                        Text(text = "Next")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            state.selectedQuiz?.let { quiz ->
                Text(text = quiz.question, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(quiz.options) {
                        if (it == state.selectedAnswer) {
                            val container = if (state.selectedAnswer == quiz.answer) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onEvent.invoke(QuizScreenViewModel.UiEvent.Answer(it)) },
                                colors = ButtonDefaults.buttonColors(containerColor = container)
                            ) {
                                Text(text = it)
                            }
                        } else {
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onEvent.invoke(QuizScreenViewModel.UiEvent.Answer(it)) },
                            ) {
                                Text(text = it)
                            }
                        }
                    }
                }
            }
        }
    }

}