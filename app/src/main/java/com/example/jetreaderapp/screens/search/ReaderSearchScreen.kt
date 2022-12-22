package com.example.jetreaderapp.screens.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.jetreaderapp.components.InputField
import com.example.jetreaderapp.components.LoadImage
import com.example.jetreaderapp.components.ReaderAppBar
import com.example.jetreaderapp.model.MBook

@Preview
@Composable
fun ReaderSearchScreen(navController: NavHostController = NavHostController(LocalContext.current),
viewModel: BookSearchViewModel = hiltViewModel()) {
    val listOfBooks = emptyList<MBook>()
    Scaffold(topBar = {
        ReaderAppBar(title = "Search Books",
            navController = navController,
        icon = Icons.Default.ArrowBack,
        showProfile = false) {
            navController.popBackStack()
        }
    }) {it
        Surface {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel
                ) { searchText ->
                    viewModel.searchBooks(query = searchText)
                    Log.d("On Search", "ReaderSearchScreen: $searchText")
                }

                RenderBookSearchList(listOfBooks)
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading : Boolean = false,
    hint : String = "Search",
    viewModel: BookSearchViewModel = hiltViewModel(),
    onSearch : (String) -> Unit = {},
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = rememberSaveable(searchQueryState.value) { searchQueryState.value.isNotBlank() }

        InputField(
            modifier = modifier,
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if(!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun RenderBookSearchList(books : List<MBook> = emptyList(), viewModel: BookSearchViewModel = hiltViewModel()) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)){
        items(books) {book->
            BookItem(book)
        }
    }
}

@Preview
@Composable
fun BookItem(book : MBook = MBook()) {
    Row(modifier = Modifier
        .padding(4.dp)
        .height(100.dp)
        .fillMaxWidth()
        .clickable { }) {
        LoadImage()
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(book.title!!, style = MaterialTheme.typography.h6, overflow = TextOverflow.Ellipsis)
            Text(book.authors!!, style = MaterialTheme.typography.subtitle1)
            Text("", style = MaterialTheme.typography.subtitle1)
            Text("", style = MaterialTheme.typography.subtitle1)
        }
    }
}

