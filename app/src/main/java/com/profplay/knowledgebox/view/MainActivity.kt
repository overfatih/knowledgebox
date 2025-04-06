package com.profplay.knowledgebox.view


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.profplay.knowledgebox.screen.CityDetailsScreen
import com.profplay.knowledgebox.screen.KnowledgePoolScreen
import com.profplay.knowledgebox.screen.MainScreen
import com.profplay.knowledgebox.screen.SettingScreen
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
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.profplay.knowledgebox.screen.ProfileScreen
import com.profplay.knowledgebox.screen.QuestionWithSTTScreen
import com.profplay.knowledgebox.screen.QuestionWithTTSScreen
import com.profplay.knowledgebox.screen.UsbScreen
import com.profplay.knowledgebox.util.UsbViewModelFactory
import com.profplay.knowledgebox.viewModel.UsbViewModel
import com.profplay.knowledgebox.helper.controldevices.UsbManagerHelper
import com.profplay.knowledgebox.screen.HowToPlayScreen
import com.profplay.knowledgebox.screen.ProfileDetailScreen
import com.profplay.knowledgebox.viewModel.ProfileViewModel


class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_RECORD_AUDIO = 1
    internal lateinit var myAuth: FirebaseAuth

    private val mainViewModel: MainViewModel by viewModels<MainViewModel> ()
    private val knowledgePoolViewModel: KnowledgePoolViewModel by viewModels<KnowledgePoolViewModel> ()
    private val cityDetailViewModel: CityDetailViewModel by viewModels<CityDetailViewModel> ()
    private val questionViewModel: QuestionViewModel by viewModels<QuestionViewModel> ()
    private val profileViewModel: ProfileViewModel by viewModels<ProfileViewModel> ()
    private lateinit var usbReceiver: BroadcastReceiver

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val usbViewModel: UsbViewModel = viewModel(factory = UsbViewModelFactory(this))
            val usbData by usbViewModel.usbData.collectAsState()
            KnowledgeBoxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding).systemBarsPadding().navigationBarsPadding()
                    ){
                        NavHost(navController= navController, startDestination = "main_screen") {
                            composable("main_screen"){
                                MainScreen(name = "Knowledge Box", navController = navController)
                            }
                            /*main screens*/
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
                                        questionViewModel=questionViewModel,
                                        correctAnswers=correntAnswers,
                                        totalAnswers=totalAnswers,
                                        cityDetailImageLink=cityDetailImageLink,
                                        onNextQuestion = {selectedOptionIndex->
                                            lifecycleScope.launch {
                                                kotlinx.coroutines.delay(2000)
                                                Log.d("ttsCompletedIndex", selectedOptionIndex.toString())
                                                questionViewModel.question.value = null

                                            }
                                        }
                                        )

                                } ?: CircularProgressIndicator(modifier = Modifier.fillMaxSize())
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

                            composable("question_with_tts_screen"){
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
                                    QuestionWithTTSScreen(
                                        question = q,
                                        correctAnswers=correntAnswers,
                                        totalAnswers=totalAnswers,
                                        cityDetailImageLink=cityDetailImageLink,
                                        questionViewModel= questionViewModel,
                                        usbViewModel = usbViewModel,
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

                            composable("profile_screen"){
                                ProfileScreen(profileViewModel, context, navController)
                            }

                            composable("profile_detail_screen"){
                                ProfileDetailScreen(navController)
                            }

                            composable("how_to_play_screen"){
                                HowToPlayScreen(navController)
                            }

                            composable("question_with_stt_screen"){
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
                                    QuestionWithSTTScreen(
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
                                SettingScreen(navController = navController)
                            }

                            /*sub screens*/
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

                            composable("usb_screen"){
                                UsbScreen(usbViewModel)
                            }
                        }
                    }
                }
            }
        }

        checkAudioPermission() // Mikrofon izni kontrolü

        usbReceiver = UsbManagerHelper.createUsbReceiver { granted ->
            if (granted) {
                Log.d("USB", "USB izni verildi!")
            } else {
                Log.d("USB", "USB izni reddedildi!")
            }
        }
        val filter = IntentFilter(UsbManagerHelper.USB_PERMISSION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(usbReceiver, filter)
        }

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


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(usbReceiver) // MainActivity’den çıkarken receiver’ı kaldır

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