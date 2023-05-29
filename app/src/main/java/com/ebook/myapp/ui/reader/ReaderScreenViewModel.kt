package com.ebook.myapp.ui.reader

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ebook.myapp.base.BaseViewModel
import com.ebook.myapp.data.source.firebase.module.ModulePackService
import com.ebook.myapp.ui.login.LoginScreenViewModel
import com.ebook.myapp.utils.populateStorageLink
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReaderScreenViewModel @Inject constructor(
): BaseViewModel<ReaderScreenViewModel.State, ReaderScreenViewModel.UiEvent>() {
    private val _path = MutableStateFlow("")

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _path.flatMapConcat {
                flow { emit(it.populateStorageLink()) }
            }.catch {
                Log.e(TAG, "observerModule: failed cause", it)
            }.collectLatest {
                if (it.isBlank()) {
                    _state.update { state ->
                        state.copy(pdfState = null)
                    }
                    return@collectLatest
                }
                _state.update { state ->
                    state.copy(
                        pdfState = VerticalPdfReaderState(
                            resource = ResourceType.Remote(
                                url = it
                            ),
                            isZoomEnable = true
                        )
                    )
                }
            }
        }
    }

    fun set(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _path.emit(path)
        }
    }

    override fun defaultState(): State = State(null)

    override fun onEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when(event) {
                is UiEvent.MoveBack -> {
                    _event.emit(Event.MoveBack)
                }
            }
        }

    }

    data class State(
        val pdfState: VerticalPdfReaderState?
    )

    sealed class UiEvent {
        object MoveBack: UiEvent()
    }

    sealed class Event {
        object MoveBack: Event()
    }

    companion object {
        const val TAG = "ReaderScreenViewModel"
    }
}