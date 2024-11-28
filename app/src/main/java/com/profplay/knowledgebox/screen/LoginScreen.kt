package com.profplay.knowledgebox.screen

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.profplay.knowledgebox.auth

@Composable
fun LoginScreen(onLoginSuccess: ()->Unit){

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var isLoginEnabled = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment= Alignment.Center
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    loginClick(username.value, password.value, context, onLoginSuccess)
                }) { Text("Giriş Yap") }

                Button(onClick = {
                    registerClick(username.value,password.value, context, onLoginSuccess)
                }) { Text("Kayıt ol") }
            }

        }
    }
}

private fun loginClick(usernameValue: String, passwordValue:String, parent: Context, onLoginSuccess:()->Unit){
    if(usernameValue.isNotEmpty() && passwordValue.isNotEmpty()){
        auth.signInWithEmailAndPassword(usernameValue,passwordValue).addOnSuccessListener{
            //ToDo: goto MainActivity with login success
            onLoginSuccess()
        }.addOnFailureListener{
            Toast.makeText(parent, it.localizedMessage ,Toast.LENGTH_LONG).show()
        }
    }else{
        Toast.makeText(parent, "Lütfen email ve parolayı giriniz!",Toast.LENGTH_LONG).show()
    }

}

private fun registerClick(usernameValue: String, passwordValue:String, parent: Context, onLoginSuccess:()->Unit){

    if(usernameValue.isNotEmpty() && passwordValue.isNotEmpty()){
        auth.createUserWithEmailAndPassword(usernameValue,passwordValue).addOnSuccessListener{
            //ToDo: goto MainActivity with register success
            onLoginSuccess()
        }.addOnFailureListener{
            Toast.makeText(parent, it.localizedMessage ,Toast.LENGTH_LONG).show()
        }
    }else{
        Toast.makeText(parent, "Lütfen email ve parolayı giriniz!",Toast.LENGTH_LONG).show()
    }

}
