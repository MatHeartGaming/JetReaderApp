package com.example.jetreaderapp.screens.details

import androidx.lifecycle.ViewModel
import com.example.jetreaderapp.data.Resource
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.repository.BookRepository2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository2: BookRepository2) : ViewModel() {

    suspend fun getBookInfo(bookId : String) : Resource<Item> {
        return repository2.getBookInfo(bookId)
    }

}