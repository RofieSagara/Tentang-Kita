package com.ebook.myapp.ui.quiz

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ebook.myapp.base.BaseViewModel
import com.ebook.myapp.data.source.Quiz
import com.ebook.myapp.data.source.firebase.quiz.QuizService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(
    private val quizService: QuizService
): BaseViewModel<QuizScreenViewModel.State, QuizScreenViewModel.UiEvent>() {
    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            quizService.observerQuizCollection()
                .catch { Log.e(TAG, "observerQuizCollection: failed", it) }
                .collectLatest {
                    _state.update { state ->
                        state.copy(quizCollection = it)
                    }
                    onEvent(UiEvent.Next)
                }
        }
    }

    override fun defaultState(): State = State(listOf(), null, "")

    override fun onEvent(event: UiEvent) {
         viewModelScope.launch(Dispatchers.IO) {
             when(event) {
                 is UiEvent.Answer -> {
                     if (_state.value.selectedAnswer.isBlank())
                         _state.update { state ->
                             state.copy(selectedAnswer = event.selectedAnswer)
                         }
                 }
                 is UiEvent.Next -> {
                     _state.update { state ->
                         val next = state.quizCollection.random()
                         val options = next.options.toMutableList()
                         options.add(next.answer)
                         options.shuffle()
                         val buildQuiz = next.copy(options = options)
                         state.copy(selectedQuiz = buildQuiz, selectedAnswer = "")
                     }
                 }
                 is UiEvent.OnBack -> {
                     _event.emit(Event.OnBack)
                 }
             }
         }
    }

    data class State(
        val quizCollection: List<Quiz>,
        val selectedQuiz: Quiz?,
        val selectedAnswer: String
    )

    sealed class UiEvent {
        class Answer(val selectedAnswer: String): UiEvent()
        object Next: UiEvent()
        object OnBack: UiEvent()
    }

    sealed class Event {
        object OnBack: Event()
    }

    companion object {
        const val TAG = "QuizScreenViewModel"
    }
}