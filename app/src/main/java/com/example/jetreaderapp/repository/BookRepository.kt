package com.example.jetreaderapp.repository

import com.example.jetreaderapp.data.DataOrException
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api : BooksApi) {

    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()

    suspend fun getBooks(searchQuery : String) : DataOrException<List<Item>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if(dataOrException.data!!.isNotEmpty()) {
                dataOrException.loading = false
            }
        }catch (exc : Exception) {
            dataOrException.e = exc
        }
        return dataOrException
    }

    suspend fun getBookInfo(bookId : String) : DataOrException<Item, Boolean, Exception> {
        val response = try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId)
        } catch (exc : Exception) {
            bookInfoDataOrException.e = exc
        } finally {
            bookInfoDataOrException.loading = false
        }
        return bookInfoDataOrException
    }

}