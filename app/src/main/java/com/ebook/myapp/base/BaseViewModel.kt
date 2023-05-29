package com.ebook.myapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State, ScreenEvent>: ViewModel() {
    protected val _state = MutableStateFlow(this.defaultState())
    val state = _state.asStateFlow()

    abstract fun defaultState(): State

    abstract fun onEvent(event: ScreenEvent)
}