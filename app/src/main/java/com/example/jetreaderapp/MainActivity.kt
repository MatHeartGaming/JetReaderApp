package com.example.jetreaderapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetreaderapp.navigation.ReaderNavigation
import com.example.jetreaderapp.ui.theme.JetReaderAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetReaderAppTheme {
                ReaderApp()
            }
        }
    }
}

fun createUser() {
    val db = FirebaseFirestore.getInstance()
    val user : MutableMap<String, Any> = HashMap()
    user["firstName"] = "Mat"
    user["lastName"] = "Buompy"
    db.collection("users").add(user).addOnSuccessListener {
        Log.d("Firestore", "createUser: ${it.id}")
    }.addOnFailureListener {
        Log.d("Firestore", "createUser Fail: ${it.localizedMessage}")
    }

}

@Composable
fun ReaderApp() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            ReaderNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetReaderAppTheme {
        ReaderNavigation()
    }
}