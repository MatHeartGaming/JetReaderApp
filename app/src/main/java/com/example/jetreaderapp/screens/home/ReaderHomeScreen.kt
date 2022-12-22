package com.example.jetreaderapp.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarBorderPurple500
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jetreaderapp.R
import com.example.jetreaderapp.components.*
import com.example.jetreaderapp.model.MBook
import com.example.jetreaderapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import okhttp3.internal.wait

@Preview
@Composable
fun Home(navController: NavHostController = NavHostController(LocalContext.current)) {
    Scaffold(topBar = {
        ReaderAppBar(title = "A. Reader", navController = navController)
    },
        floatingActionButton = {
            FabContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        it
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavHostController) {
    val listOfBooks = listOf(
        MBook(id = "sds", title = "Fifty shades of gray", authors = "Boh", notes = null),
        MBook(id = "sds2", title = "Fifty shades of gray 2", authors = "Boh 2", notes = null),
        MBook(id = "sds3", title = "Fifty shades of gray 3", authors = "Boh 3", notes = null),
        MBook(id = "sds4", title = "Fifty shades of gray 4", authors = "Boh 4", notes = null),
        MBook(id = "sds5", title = "Fifty shades of gray 5", authors = "Boh 5", notes = null),
    )
    val currentUser = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!currentUser.isNullOrEmpty())
        currentUser.split("@").first()
    else "Not Available"
    Column(
        Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(Modifier.align(Alignment.Start), verticalAlignment = Alignment.CenterVertically) {
            TitleSection(label = "You're reading activity right now")
            Spacer(modifier = Modifier.fillMaxWidth(0.5f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile",
                    tint = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        })
                Text(
                    currentUserName, modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Divider()
            }
        }
        ReadingRightNowArea(navController = navController)
        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavHostController) {
    HorizontalScrollableComponent(listOfBooks) {
        // TODO: Go to details
        Log.d("On Pressed Book", "BookListArea: Go to details $")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed : (String)-> Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
        .horizontalScroll(scrollState)) {
        for(book in listOfBooks) {
            ListCard(book) {
                onCardPressed(it)
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books : List<MBook> = listOf(), navController: NavHostController) {
    ListCard()
}



