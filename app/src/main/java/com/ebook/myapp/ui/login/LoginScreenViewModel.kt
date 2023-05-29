package com.ebook.myapp.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ebook.myapp.base.BaseViewModel
import com.ebook.myapp.data.source.User
import com.ebook.myapp.data.source.firebase.user.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userService: UserService
): BaseViewModel<LoginScreenViewModel.State, LoginScreenViewModel.UiEvent>() {
    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userService.observerUserID()
                .onEach {
                    _state.update { state -> state.copy(currentUserID = it) }
                }.catch {
                    _state.update { state -> state.copy(currentUserID = "") }
                }.collect()
        }
    }

    override fun defaultState(): State = State("", true)

    override fun onEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when(event) {
                is UiEvent.LoginUser -> {
                    loginPassword(event.email, event.password)
                }
                is UiEvent.MoveToLogin -> {
                    _state.update { state ->
                        state.copy(isLogin = true)
                    }
                }
                is UiEvent.MoveToRegister -> {
                    _state.update { state ->
                        state.copy(isLogin = false)
                    }
                }
                is UiEvent.RegisterUser -> {
                    registerUser(event.email, event.password)
                }
            }
        }
    }

    private fun loginPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userService.login(email, password)
                .onEach { _event.emit(Event.Idle) }
                .onEach { _event.emit(Event.MoveToHome) }
                .catch { _event.emit(Event.Error(it.message.orEmpty())) }
                .collect()
        }
    }

    private fun registerUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userService.register(email, password)
                .onEach { _event.emit(Event.Idle) }
                .onEach { _event.emit(Event.MoveToHome) }
                .catch { _event.emit(Event.Error(it.message.orEmpty())) }
                .collect()
        }
    }

    data class State(
        val currentUserID: String,
        val isLogin: Boolean,
    )

    sealed class UiEvent {
        class LoginUser(val email: String, val password: String): UiEvent()
        class RegisterUser(val email: String, val password: String): UiEvent()
        object MoveToRegister: UiEvent()
        object MoveToLogin: UiEvent()
    }

    sealed class Event {
        object Idle: Event()
        class Error(val message: String): Event()
        object MoveToHome: Event()
    }

    companion object {
        const val TAG = "LoginScreenViewModel"
    }
}

