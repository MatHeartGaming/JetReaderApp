package com.example.jetreaderapp.repository

import com.example.jetreaderapp.data.DataOrException
import com.example.jetreaderapp.model.MBook
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val queryBook : Query) {

    suspend fun getAllBooksFromDatabase() : DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map {docSnap ->
                docSnap.toObject(MBook::class.java)!!
            }
            if(!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        }catch (exc : Exception) {
            dataOrException.e = exc
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

}