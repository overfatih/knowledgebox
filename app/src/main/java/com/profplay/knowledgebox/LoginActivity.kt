package com.profplay.knowledgebox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.screen.LoginScreen
import com.profplay.knowledgebox.ui.theme.KnowledgeBoxTheme

internal lateinit var auth: FirebaseAuth

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            KnowledgeBoxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)
                    ){
                        NavHost(navController= navController, startDestination = "login_screen") {
                            composable("login_screen"){
                                LoginScreen(onLoginSuccess = {
                                    navigateToMainActivity() // Login başarılı olduğunda MainActivity'ye geçiş
                                })
                            }
                        }

                    }
                }
            }
        }

        auth = Firebase.auth
        if(auth.currentUser != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            finish()
            startActivity(intent)

        }

    }

    private fun navigateToMainActivity() {
        // MainActivity'ye geçiş yapmak için Intent kullanıyoruz
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()  // LoginActivity'yi kapatıyoruz ki geri butonu ile tekrar açılmasın
    }
}

@Preview(showBackground = true)
@Composable
fun LoginActivityPreview() {
    KnowledgeBoxTheme {
        LoginScreen(onLoginSuccess = { })

    }
}