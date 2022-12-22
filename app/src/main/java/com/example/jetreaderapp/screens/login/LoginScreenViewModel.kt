package com.example.jetreaderapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {

    //val loadState = MutableStateFlow(LoadingState.IDLE)
    private val auth : FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email : String, password : String, function: () -> Unit)
    = viewModelScope.launch {
        try {
            if(_loading.value == false) {
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task->
                    if(task.isSuccessful) {
                        function()
                    } else {
                        Log.d("FB", "createUserWithEmailAndPassword: Fail signing up ${task.exception}")
                    }
                    _loading.value = false
                }
            }
        }catch (ex : Exception) {
            Log.d("SingUp Error", "createUserWithEmailAndPassword: ${ex.localizedMessage}")
        }
    }

    fun singInWithEmailAndPassword(email : String, password : String, function : ()-> Unit)
    = viewModelScope.launch {

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FB", "singInWithEmailAndPassword: Success signing in")
                    function()
                } else {
                    Log.d("FB", "singInWithEmailAndPassword: Fail signing in")
                }
            }
        } catch (ex: Exception) {
            Log.d("SingIn Error", "singInWithEmailAndPassword: ${ex.localizedMessage}")
        }
    }
}