package com.ebook.myapp.data.source.firebase.user

import com.ebook.myapp.data.source.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject

class UserService @Inject constructor() {
    private val firebaseAuth = Firebase.auth
    private val fireStore = Firebase.firestore

    // emit userUID if there is no user login it send the empty
    fun observerUserID() = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.uid.orEmpty()) }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    fun login(email: String, password: String) = flow {
        val task = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        if (task.user != null) {
            val userData = task.user ?: throw Throwable("user its not found!")
            val timeNow = Instant.now().toEpochMilli()
            val user = User(
                id = userData.uid,
                email = userData.email.orEmpty(),
                profile = userData.photoUrl.toString(),
                fullName = userData.displayName.toString(),
                isAdmin = false,
                createdAt = timeNow,
                updatedAt = timeNow
            )
            val currentUser = fireStore.collection("users").document(user.id).get().await()
            if (currentUser == null) {
                fireStore.collection("users").document(user.id).set(user).await()
            }
        } else {
            throw Throwable("failed when try to signIn")
        }

        emit(Unit)
    }

    fun register(email: String, password: String) = flow {
        val task = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val userData = task.user ?: throw Throwable("user failed to get while register")
        val timeNow = Instant.now().toEpochMilli()
        val user = User(
            id = userData.uid,
            email = userData.email.orEmpty(),
            profile = userData.photoUrl.toString(),
            fullName = userData.displayName.toString(),
            isAdmin = false,
            createdAt = timeNow,
            updatedAt = timeNow
        )
        val currentUser = fireStore.collection("users").document(user.id).get().await()
        if (currentUser == null) {
            fireStore.collection("users").document(user.id).set(user).await()
        }
        emit(Unit)
    }
}