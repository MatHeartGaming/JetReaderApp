package com.example.jetreaderapp.screens.search

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.model.MBook

@Preview
@Composable
fun ReaderSearchScreen(navController: NavHostController = NavHostController(LocalContext.current),
viewModel: BooksSearchViewModel = hiltViewModel()) {

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

                RenderBookSearchList(viewModel.list)
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
    viewModel: BooksSearchViewModel = hiltViewModel(),
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
fun RenderBookSearchList(books: List<Item> = emptyList(), viewModel: BooksSearchViewModel = hiltViewModel()) {
    if(viewModel.isLoading) {
        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(Modifier.size(80.dp))
        }
    } else {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)){
            items(books) {book->
                BookItem(book)
            }
        }
    }

}

@Composable
fun BookItem(book: Item) {
    Row(modifier = Modifier
        .padding(4.dp)
        .height(100.dp)
        .fillMaxWidth()
        .clickable { }) {
        LoadImage(book.volumeInfo.imageLinks.smallThumbnail)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(book.volumeInfo.title, style = MaterialTheme.typography.h6, overflow = TextOverflow.Ellipsis)
            Text("Author(s): ${book.volumeInfo.authors}", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            Text("Date: ${book.volumeInfo.publishedDate}", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            Text("Category: ${book.volumeInfo.categories}", fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
}

