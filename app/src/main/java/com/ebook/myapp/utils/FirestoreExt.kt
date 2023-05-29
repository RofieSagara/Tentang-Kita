package com.ebook.myapp.utils

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

suspend fun StorageReference.populateStorageLink(): String {
    val linkUrl = kotlin.runCatching { this.downloadUrl.await()  }
        .getOrElse { Uri.EMPTY }
    return linkUrl.toString()
}

suspend fun String.populateStorageLink(): String {
    val linkUrl = kotlin.runCatching { Firebase.storage.getReferenceFromUrl(this).downloadUrl.await()  }
        .getOrElse { Uri.EMPTY }
    return linkUrl.toString()
}