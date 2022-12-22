package com.example.jetreaderapp.repository

import com.example.jetreaderapp.data.Resource
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.network.BooksApi
import javax.inject.Inject

class BookRepository2 @Inject constructor(private val api : BooksApi) {

    suspend fun getBooks(searchQuery : String) : Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuery).items
            if(itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        }catch (exc : Exception) {
            Resource.Error(message = exc.localizedMessage)
        }
    }

    suspend fun getBookInfo(bookId : String) : Resource<Item> {
        return try {
            Resource.Loading(data = true)
            val data = api.getBookInfo(bookId)
            Resource.Success(data)
        } catch (exc : Exception) {
            Resource.Error(message = exc.localizedMessage)
        } finally {
            Resource.Loading(data = false)
        }

    }

}