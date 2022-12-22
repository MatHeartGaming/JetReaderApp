package com.example.jetreaderapp.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetreaderapp.data.Resource
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.repository.BookRepository2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksSearchViewModel @Inject constructor(private val repository: BookRepository2) : ViewModel(){

    var list : List<Item> by mutableStateOf(listOf())
    var isLoading : Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if(query.isBlank()) return@launch
            isLoading = true
            try {
                when(val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data!!
                        if(list.isNotEmpty()) isLoading = false
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.d("Error", "searchBooks: Failed to get books $response")
                    }
                    else ->{isLoading = false}
                }
            }catch (exc : Exception) {
                Log.d("Error", "searchBooks: Failed to get books ${exc.localizedMessage}")
                isLoading = false
            }
        }
    }

}