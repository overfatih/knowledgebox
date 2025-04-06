package com.profplay.knowledgebox.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val user by profileviewModel.user.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    user?.let {
                        Text(text = "E-posta: ${it.email ?: "Bilinmiyor"}")
                    } ?: Text("Kullanıcı bilgisi alınamadı.")
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
                    //MainButton("Setting1",Icons.Filled.TagFaces, isEnabled = false){navController.navigate("main_screen")}
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
                    //MainButton("Setting2",Icons.Filled.TagFaces, isEnabled = false){ navController.navigate("main_screen") }
                    MainButton("Manage Account",Icons.Filled.ManageAccounts){ navController.navigate("profile_detail_screen") }
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    //MainButton("Setting3", Icons.Filled.TagFaces, isEnabled = false){ navController.navigate("main_screen") }
                    MainButton("Back", Icons.Filled.ArrowBackIosNew){ navController.navigate("main_screen") }
                }
            }
        }
    }
}