package com.ebook.myapp.data.source

data class Quiz(
    val id: String = "",
    val question: String = "",
    val answer: String = "",
    val options: List<String> = listOf()
)