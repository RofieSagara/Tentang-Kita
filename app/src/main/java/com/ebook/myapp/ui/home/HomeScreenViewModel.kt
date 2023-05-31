package com.ebook.myapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebook.myapp.base.BaseViewModel
import com.ebook.myapp.data.source.Banner
import com.ebook.myapp.data.source.ModulePack
import com.ebook.myapp.data.source.Quiz
import com.ebook.myapp.data.source.firebase.banner.BannerService
import com.ebook.myapp.data.source.firebase.module.ModulePackService
import com.ebook.myapp.data.source.firebase.quiz.QuizService
import com.ebook.myapp.utils.populateStorageLink
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val modulePackService: ModulePackService,
    private val bannerService: BannerService,
    private val quizService: QuizService
): BaseViewModel<HomeScreenViewModel.State, HomeScreenViewModel.UiEvent>() {

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            modulePackService.observerModulePackCollection()
                .catch { Log.e(TAG, "observerModulePackCollection: failed cause", it) }
                .collectLatest {
                    _state.update { state ->
                        state.copy(modulePackCollection = it)
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            bannerService.observerBannerCollection()
                .catch { Log.e(TAG, "observerBannerCollection: failed cause", it) }
                .collectLatest {
                    _state.update { state ->
                        state.copy(bannerCollection = it)
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            quizService.observerQuizCollection()
                .catch { Log.e(TAG, "observerQuizCollection: failed cause", it) }
                .collectLatest {
                    _state.update { state ->
                        state.copy(quizCollection = it)
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val link = "gs://tentang-kita-74876.appspot.com/head_thumbler.png".populateStorageLink()
            _state.update { state ->
                state.copy(headBanner = link)
            }
        }
    }

    override fun defaultState(): State = State(modulePackCollection = listOf(), bannerCollection = listOf(), quizCollection = listOf())

    override fun onEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when(event) {
                is UiEvent.MoveToModule -> {
                    _event.emit(Event.MoveToModule(event.pack.id))
                }
                is UiEvent.MoveToReader -> {
                    _event.emit(Event.MoveToReader(event.banner.file))
                }
                is UiEvent.MoveToQuiz -> {
                    _event.emit(Event.MoveToQuiz)
                }
                is UiEvent.LogOut -> {
                    Firebase.auth.signOut()
                }
            }
        }
    }

    data class State(
        val modulePackCollection: List<ModulePack>,
        val bannerCollection: List<Banner>,
        val quizCollection: List<Quiz>,
        val headBanner: String = ""
    )

    sealed class UiEvent {
        class MoveToModule(val pack: ModulePack): UiEvent()
        class MoveToReader(val banner: Banner): UiEvent()
        object MoveToQuiz: UiEvent()
        object LogOut: UiEvent()
    }

    sealed class Event {
        class MoveToModule(val packID: String): Event()
        class MoveToReader(val path: String): Event()
        object MoveToQuiz: Event()
    }

    companion object {
        const val TAG = "HomeScreenViewModel"
    }
}