package com.example.jetreaderapp.screens.details

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.jetreaderapp.ReaderApp
import com.example.jetreaderapp.components.LoadImage
import com.example.jetreaderapp.components.ReaderAppBar
import com.example.jetreaderapp.components.RoundedButton
import com.example.jetreaderapp.data.Resource
import com.example.jetreaderapp.model.Item
import com.example.jetreaderapp.model.MBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String = "",
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Details",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false,
        ) {
            navController.popBackStack()
        }
    }) {
        it
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId)
                }.value
                if(bookInfo.data == null) {
                    CircularProgressIndicator(Modifier.size(50.dp))
                } else {
                    ShowBookDetails(bookInfo, navController)
                }
            }
        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavHostController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    val context = LocalContext.current

    //Get Screen Dimension
    val localDims = LocalContext.current.resources.displayMetrics

    Column(
        Modifier
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.padding(34.dp),
            shape = CircleShape,
            elevation = 4.dp
        ) {
            LoadImage(
                modifier = Modifier
                    .size(90.dp)
                    .padding(1.dp), bookData?.imageLinks?.thumbnail ?: ""
            )

        }

        Text(bookData!!.title)
        Text("Authors ${bookData.authors}")
        Text("Page Count ${bookData.pageCount}")
        Text("Categories ${bookData.categories}", style = MaterialTheme.typography.subtitle1,
        overflow = TextOverflow.Ellipsis)
        Text("Published ${bookData.publishedDate}", style = MaterialTheme.typography.subtitle1)

        Spacer(Modifier.height(5.dp))
        
        val cleanDescr = HtmlCompat.fromHtml(bookData.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
            .toString()
        LazyColumn{
            item {
                Text(cleanDescr)
            }
        }

        Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceAround) {
            RoundedButton("Save") {
                // Save this book to Firestore FB
                val book = MBook(title = bookData.title,
                    authors = bookData.authors.toString(),
                    description = bookData.description,
                    categories = bookData.categories.toString(),
                    notes = "",
                    photoUrl = bookData.imageLinks.thumbnail,
                    pageCount = bookData.pageCount.toString(),
                    rating = 0.0f,
                    googleBookId = googleBookId,
                    userId = FirebaseAuth.getInstance().currentUser?.uid,
                    )
                saveToFirebase(book) {
                    navController.popBackStack()
                    Toast.makeText(context, "Book ${book.title} saved successfully", Toast.LENGTH_SHORT).show()
                }
            }
            Spacer(modifier = Modifier.width(25.dp))
            RoundedButton("Cancel"){
                navController.popBackStack()
            }
        }
    }


}

fun saveToFirebase(book: MBook = MBook(),
onSuccessFunc : ()-> Unit = {}) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if(book.toString().isNotBlank()) {
        dbCollection.add(book).addOnSuccessListener {documentRef->
            val docId = documentRef.id
            dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener {task->
                    if(task.isSuccessful) {
                        onSuccessFunc()
                    }
                }.addOnFailureListener {
                    Log.d("Error DB", "saveToFirebase: ${it.localizedMessage}")
                }
        }
    }
}
