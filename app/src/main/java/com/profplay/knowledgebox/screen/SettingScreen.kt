package com.profplay.knowledgebox.screen

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.profplay.knowledgebox.view.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Ayarlar")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.inverseSurface)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    MainButton(
                        "USB Control Device",
                        Icons.Filled.Usb
                    ) { navController.navigate("usb_screen") }
                    /*MainButton("Setting4", Icons.Filled.Lock, isEnabled = true){
                    navController.navigate("main_screen")
                    Toast.makeText(context, "Bu özellik yakında eklenecek", Toast.LENGTH_SHORT).show()}*/
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    //MainButton("Setting2",Icons.Filled.Build, isEnabled = false){ navController.navigate("main_screen") }
                    MainButton(
                        "Profile",
                        Icons.Filled.Person
                    ) { navController.navigate("profile_screen") }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(1f)
                        .padding(bottom = 50.dp)
                ) {
                    //MainButton("Setting3", Icons.Filled.HeadsetMic, isEnabled = false){ navController.navigate("main_screen") }
                    MainButton(
                        "Back",
                        Icons.Filled.ArrowBackIosNew
                    ) { navController.navigate("main_screen") }
                }
            }
        }
    }
}