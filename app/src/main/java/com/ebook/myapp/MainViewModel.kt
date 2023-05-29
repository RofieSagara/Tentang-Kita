package com.ebook.myapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebook.myapp.data.source.firebase.user.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userService: UserService
): ViewModel() {

    val userUID = userService.observerUserID()
        .stateIn(viewModelScope, SharingStarted.Lazily, "")
}