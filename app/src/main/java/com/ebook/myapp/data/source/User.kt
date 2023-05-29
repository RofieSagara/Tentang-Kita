package com.ebook.myapp.data.source

data class User(
    val id: String = "",
    val email: String = "",
    val profile: String = "",
    val fullName: String = "",
    val isAdmin: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)