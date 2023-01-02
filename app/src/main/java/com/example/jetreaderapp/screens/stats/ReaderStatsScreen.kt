package com.example.jetreaderapp.screens.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.jetreaderapp.components.BookRating
import com.example.jetreaderapp.components.ReaderAppBar
import com.example.jetreaderapp.model.MBook
import com.example.jetreaderapp.screens.home.HomeScreenViewModel
import com.example.jetreaderapp.screens.search.BookItem
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@Composable
fun ReaderStatsScreen(navController: NavHostController,
                      viewModel: HomeScreenViewModel = hiltViewModel()) {
    var books : List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderAppBar(title = "Book Stats",
            navController = navController,
        showProfile = false,
            icon = Icons.Default.ArrowBack
        ) {
            navController.popBackStack()
        }
    }) {it
        Surface {
            books = if(!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter {mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column {
                Box(modifier = Modifier
                    .size(45.dp)
                    .padding(2.dp)) {
                    Icon(imageVector = Icons.Sharp.Person, contentDescription = "icon")
                }
                Text("Hi, ${currentUser?.email.toString().split("@").first().uppercase(Locale.ROOT)}")
            }

            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = CircleShape,
                elevation = 5.dp,
            ) {
                val readBooksList : List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                    books.filter {mBook ->
                        (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                    }
                } else {
                    emptyList()
                }

                val readingBook = books.filter {mBook ->
                    (mBook.startedReading != null && mBook.finishedReading == null)
                }

                Column(Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.Start) {
                    Text("Your Stats", style = MaterialTheme.typography.h5)
                    Divider()
                    Text("You're reading: ${readingBook.size} books")
                    Text("You've read: ${readBooksList.size} books")
                }

            }

            if(viewModel.data.value.loading == true) {
                CircularProgressIndicator(Modifier.size(60.dp))
            } else {
                Divider()
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                    val readBooks : List<MBook> = if(!viewModel.data.value.data.isNullOrEmpty()) {
                        viewModel.data.value.data!!.filter {mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    } else {
                        emptyList()
                    }

                    items(items = readBooks) {book->
                        BookItem(book = book, navController = navController)
                    }

                }
            }

        }
    }
}
