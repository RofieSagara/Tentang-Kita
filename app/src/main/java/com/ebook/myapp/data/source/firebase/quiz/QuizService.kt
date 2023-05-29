package com.ebook.myapp.data.source.firebase.quiz

import com.ebook.myapp.data.source.Quiz
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizService @Inject constructor() {
    private val fireStore = Firebase.firestore

    fun observerQuizCollection() = flow {
        val task = fireStore.collection("quiz").get().await()
        val quizData = task.toObjects(Quiz::class.java)
        emit(quizData)
    }
}