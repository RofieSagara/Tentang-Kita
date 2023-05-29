package com.ebook.myapp.data.source.firebase.banner

import com.ebook.myapp.data.source.Banner
import com.ebook.myapp.utils.populateStorageLink
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class BannerService @Inject constructor(){
    private val fireStore = Firebase.firestore


    fun observerBannerCollection() = flow {
        val task = fireStore.collection("banner").get().await()
        val bannerCollectionData = task.toObjects(Banner::class.java)
        emit(bannerCollectionData)
    }.flatMapConcat {
        flow { emit(it.map { it.copy(photo = it.photo.populateStorageLink()) }) }
    }
}