package com.profplay.knowledgebox.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.profplay.knowledgebox.viewModel.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import com.profplay.knowledgebox.R

@Composable
fun LoginScreen(onLoginSuccess: ()->Unit){

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val viewModel : LoginViewModel = viewModel()

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment= Alignment.Center
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // İkonu görüntüle
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground), // İkonunuzun adını buraya girin
                contentDescription = "Uygulama İkonu",
                modifier = Modifier.height(100.dp)

            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(username.value,
                placeholder = {
                    Text("Email gir")
                }, onValueChange = {
                    username.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(password.value,
                placeholder = {
                    Text("Parola Gir")
                },
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    password.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    viewModel.loginClick(username.value,password.value,onLoginSuccess)
                }) { Text("Giriş Yap") }

                Button(onClick = {
                    viewModel.registerClick(username.value,password.value,onLoginSuccess)
                }) { Text("Kayıt ol") }
            }
        }
    }
}

