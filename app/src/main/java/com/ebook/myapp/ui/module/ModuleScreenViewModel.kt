package com.ebook.myapp.ui.module

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ebook.myapp.base.BaseViewModel
import com.ebook.myapp.data.source.Module
import com.ebook.myapp.data.source.ModulePack
import com.ebook.myapp.data.source.firebase.module.ModulePackService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ModuleScreenViewModel @Inject constructor(
    private val modulePackService: ModulePackService
): BaseViewModel<ModuleScreenViewModel.State, ModuleScreenViewModel.UiEvent>() {
    private val _modulePackID = MutableStateFlow("")

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _modulePackID.flatMapConcat {
                if (it.isBlank()) return@flatMapConcat flowOf(listOf<Module>())

                modulePackService.observerModuleCollection(it)
            }.catch { Log.e(TAG, "observerModuleCollection: failed cause", it) }
                .collectLatest {
                    _state.update { state ->
                        state.copy(moduleCollection = it)
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _modulePackID.flatMapConcat {
                if (it.isBlank()) return@flatMapConcat flowOf(null)

                modulePackService.observerModulePack(it)
            }.catch { Log.e(TAG, "observerModulePack: failed cause", it) }
                .collectLatest {
                    _state.update { state ->
                        state.copy(modulePack = it)
                    }
                }
        }
    }

    fun set(packID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _modulePackID.emit(packID)
        }
    }


    override fun defaultState(): State = State(listOf(), null)

    override fun onEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when(event) {
                is UiEvent.MoveToReader -> {
                    _event.emit(Event.MoveToReader(event.module.file))
                }
                is UiEvent.MoveBack -> {
                    _event.emit(Event.MoveBack)
                }
            }
        }
    }

    data class State(
        val moduleCollection: List<Module>,
        val modulePack: ModulePack?
    )

    sealed class UiEvent {
        class MoveToReader(val module: Module): UiEvent()
        object MoveBack: UiEvent()
    }

    sealed class Event {
        class MoveToReader(val path: String): Event()
        object MoveBack: Event()
    }

    companion object {
        const val TAG = "ModuleScreenViewModel"
    }
}