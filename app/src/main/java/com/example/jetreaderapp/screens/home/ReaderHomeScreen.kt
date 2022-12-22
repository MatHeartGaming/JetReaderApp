package com.example.jetreaderapp.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetreaderapp.model.MBook
import com.example.jetreaderapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Preview
@Composable
fun Home(navController: NavHostController = NavHostController(LocalContext.current)) {
    Scaffold(topBar = {
        ReaderAppBar(title = "A. Reader", navController = navController)
    },
    floatingActionButton = {
        FabContent{}
    }) {it
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavHostController) {
    Column(Modifier.padding(2.dp),
    verticalArrangement = Arrangement.SpaceEvenly) {
        Row(Modifier.align(Alignment.Start)) {
            TitleSection(label = "You're reading \" activity right now")
        }
    }
}

@Composable
fun FabContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onTap()
        },
    backgroundColor = MaterialTheme.colors.secondary
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a Book",
            tint = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
fun ReaderAppBar(
    title : String,
    showProfile : Boolean = true,
    navController: NavHostController,
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if(showProfile) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Logo Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.6f))
                }
                Text(title, color = Color.Red.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold, fontSize = 17.sp)

            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run{
                    navController.navigate(ReaderScreens.LoginScreen.name) {
                        popUpTo(ReaderScreens.ReaderHomeScreen.name) {
                            inclusive = true
                        }
                    }
                }
            }) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "Log out",
                tint = Color.Green.copy(alpha = 0.4f))
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    )
}

@Composable
fun ReadingRightNowArea(books : List<MBook>, navController: NavHostController) {

}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label : String) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(label, fontSize = 19.sp, fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Left)
        }
    }
}
