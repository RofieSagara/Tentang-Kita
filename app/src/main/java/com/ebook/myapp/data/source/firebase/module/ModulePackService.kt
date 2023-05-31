package com.ebook.myapp.data.source.firebase.module

import android.net.Uri
import com.ebook.myapp.data.source.Module
import com.ebook.myapp.data.source.ModulePack
import com.ebook.myapp.utils.populateStorageLink
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ModulePackService @Inject constructor(){
    private val fireStore = Firebase.firestore
    private val storage = Firebase.storage.reference

    fun observerModulePackCollection() = flow {
        val result = fireStore.collection("pack").get().await()
        val modulePackDataCollection = result.toObjects(ModulePack::class.java)
        emit(modulePackDataCollection)
    }.flatMapConcat {
        flow { emit(it.map { it.copy(photo = it.photo.populateStorageLink()) }.sortedBy { it.index }) }
    }

    fun observerModuleCollection(packID: String) = flow {
        val result = fireStore.collection("pack").document(packID)
            .collection("module").get().await()
        val moduleDataCollection = result.toObjects(Module::class.java)
        emit(moduleDataCollection)
    }.flatMapConcat {
        flow { emit(it.map { it.copy(photo = it.photo.populateStorageLink()) }.sortedBy { it.index }) }
    }

    fun observerModule(packID: String, moduleID: String) = flow {
        val result = fireStore.collection("pack").document(packID)
            .collection("module").document(moduleID).get().await()
        val moduleData = result.toObject(Module::class.java)
        emit(moduleData)
    }.flatMapConcat {
        flow { emit(it?.copy(photo = it.photo)) }
    }

    fun observerModulePack(packID: String)  = flow {
        val result = fireStore.collection("pack").document(packID).get().await()
        val modulePackData = result.toObject(ModulePack::class.java)
        emit(modulePackData)
    }.flatMapConcat {
        flow { emit(it?.copy(photo = it.photo)) }
    }
}