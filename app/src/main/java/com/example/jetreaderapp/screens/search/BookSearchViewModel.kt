package com.example.jetreaderapp.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetreaderapp.data.DataOrException
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    private val listOfBooks : MutableState<DataOrException<List<Item>, Boolean, Exception>>
    = mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(query.isEmpty()) return@launch

            //listOfBooks.value.loading = true //Provokes crash
            listOfBooks.value = repository.getBooks(query)
            Log.d("SearchBooks", "searchBooks: ${listOfBooks.value.data.toString()}")
            if(listOfBooks.value.data.isNullOrEmpty()) listOfBooks.value.loading = false

        }
    }

}