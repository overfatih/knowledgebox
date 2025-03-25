package com.profplay.knowledgebox.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.view.LoginActivity
import com.profplay.knowledgebox.view.MainActivity
import com.profplay.knowledgebox.view.myAuth
import com.profplay.knowledgebox.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(profileviewModel: ProfileViewModel, context: Context){
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar (
                actions = {
                    IconButton(onClick = { /* do something */
                        myAuth = Firebase.auth
                        myAuth.signOut()
                        profileviewModel.navigateToLoginActivity(context)
                    }) {
                        Icon(
                            Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Cancel, Go Back"
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.PlayArrow,
                            contentDescription = "Voice It!"
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Filled.ThumbUp,
                            contentDescription = "Confirm"
                        )
                    }
                }
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = "Here is Profile Screen",
                color = Color.Black,
            )
        }
    }
}