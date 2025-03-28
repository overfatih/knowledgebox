package com.profplay.knowledgebox.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.view.myAuth
import com.profplay.knowledgebox.viewModel.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileviewModel: ProfileViewModel, context: Context, navController: NavController){
    val context = LocalContext.current
    val userName = profileviewModel.getCurrentUserName()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(56.dp), // Yüksekliği sabitle
                title = {
                    Text(text = userName ?: "Misafir", color = Color.Black)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.inverseSurface)
            ) {
                Row (
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    MainButton("USB Control Device",Icons.Filled.TagFaces, isEnabled = false){navController.navigate("main_screen")}
                    MainButton("Exit Profile", Icons.Filled.ExitToApp){
                        myAuth = Firebase.auth
                        myAuth.signOut()
                        profileviewModel.navigateToLoginActivity(context)
                    }
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    MainButton("Setting2",Icons.Filled.TagFaces, isEnabled = false){ navController.navigate("main_screen") }
                    MainButton("Setting5",Icons.Filled.Person){ navController.navigate("profile_screen") }
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    MainButton("Setting3", Icons.Filled.TagFaces, isEnabled = false){ navController.navigate("main_screen") }
                    MainButton("Setting6", Icons.Filled.ArrowBackIosNew){ navController.navigate("main_screen") }
                }
            }
        }
    }
}