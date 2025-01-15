package com.profplay.knowledgebox.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.screen.CityDetailsScreen
import com.profplay.knowledgebox.screen.KnowledgePoolScreen
import com.profplay.knowledgebox.screen.MainScreen
import com.profplay.knowledgebox.screen.SettingScreen
import com.profplay.knowledgebox.screen.QuestionScreenWithSTTScreen
import com.profplay.knowledgebox.ui.theme.KnowledgeBoxTheme
import com.profplay.knowledgebox.viewModel.CityDetailViewModel
import com.profplay.knowledgebox.viewModel.KnowledgePoolViewModel
import com.profplay.knowledgebox.viewModel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.profplay.knowledgebox.screen.QuestionScreen
import com.profplay.knowledgebox.viewModel.QuestionViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.Manifest

class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_RECORD_AUDIO = 1
    internal lateinit var myAuth: FirebaseAuth

    private val mainViewModel: MainViewModel by viewModels<MainViewModel> ()
    private val knowledgePoolViewModel: KnowledgePoolViewModel by viewModels<KnowledgePoolViewModel> ()
    private val cityDetailViewModel: CityDetailViewModel by viewModels<CityDetailViewModel> ()
    private val questionViewModel: QuestionViewModel by viewModels<QuestionViewModel> ()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            KnowledgeBoxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                         BottomAppBar (
                            actions = {
                                IconButton(onClick = { /* do something */
                                    myAuth = Firebase.auth
                                    myAuth.signOut()
                                    navigateToLoginActivity()
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
                    ){
                        NavHost(navController= navController, startDestination = "main_screen") {
                            composable("main_screen"){
                                MainScreen(name = "Knowledge Box", navController = navController)
                            }

                            composable("knowledge_pool_screen") {
                                knowledgePoolViewModel.getAllCities()
                                val cityList by remember {
                                    knowledgePoolViewModel.cityList
                                }
                                KnowledgePoolScreen(
                                    cityList = cityList,
                                    navController = navController
                                )
                            }

                            composable("question_screen") {
                                if (questionViewModel.question.value == null) {
                                    questionViewModel.generateQuestion()
                                }
                                val question by remember {
                                    questionViewModel.question
                                }

                                val totalAnswers by remember {
                                    questionViewModel.totalAnswers
                                }
                                val correntAnswers by remember {
                                    questionViewModel.correctAnswers
                                }
                                val cityDetailImageLink by remember {
                                    questionViewModel.cityDetailImageLink
                                }
                                question?.let { q ->
                                    QuestionScreen(
                                        question = q,
                                        correctAnswers=correntAnswers,
                                        totalAnswers=totalAnswers,
                                        cityDetailImageLink=cityDetailImageLink,
                                        questionViewModel= questionViewModel,
                                        onNextQuestion = {selectedOptionIndex->
                                            questionViewModel.calculateScore(selectedOptionIndex, q.correctAnswer)
                                            lifecycleScope.launch {
                                                kotlinx.coroutines.delay(2000)
                                                questionViewModel.question.value = null
                                            }
                                        }
                                        )

                                } ?: CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                            }
                            composable("setting_screen"){
                                SettingScreen()
                            }
                            composable("question_screen_with_sst_screen"){
                                    QuestionScreenWithSTTScreen()
                            }
                            composable("city_details_screen/{cityId}",
                                arguments = listOf(
                                    navArgument("cityId") {
                                        type = NavType.StringType
                                    }
                                )
                            ) {
                                val cityIdString = remember {
                                    it.arguments?.getString("cityId")
                                }
                                LaunchedEffect(cityIdString) {
                                    cityDetailViewModel.getCity(cityIdString?.toInt()?:1)
                                    cityDetailViewModel.getCityWithDetails(cityIdString?.toInt()?:1)
                                }

                                val selectedCity = cityDetailViewModel.selectedCity.value
                                val cityWithDetails = cityDetailViewModel.selectedCityWithDetails.value

                                CityDetailsScreen(selectedCity, cityWithDetails)
                            }
                        }
                    }
                }
            }
        }
        // Mikrofon izni kontrolü
        checkAudioPermission()

    }
    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_RECORD_AUDIO
            )
        }
    }

    private fun navigateToLoginActivity() {
        // LoginActivity'ye geçiş yapmak için Intent kullanıyoruz
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
          // MainActivity'yi kapatıyoruz ki geri butonu ile tekrar açılmasın
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KnowledgeBoxTheme {
        val navControllerPreview = rememberNavController()
        MainScreen("Knowledge Box", navControllerPreview)
    }
}