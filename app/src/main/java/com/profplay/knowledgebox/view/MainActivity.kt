package com.profplay.knowledgebox.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.screen.CityDetailsScreen
import com.profplay.knowledgebox.screen.GameScreen
import com.profplay.knowledgebox.screen.KnowledgePoolScreen
import com.profplay.knowledgebox.screen.MainScreen
import com.profplay.knowledgebox.screen.SettingScreen
import com.profplay.knowledgebox.ui.theme.KnowledgeBoxTheme

class MainActivity : ComponentActivity() {

    internal lateinit var myAuth: FirebaseAuth
    val cityDetail: CityDetail = CityDetail(type = "type", name = "detailName",image = null)
    val  city: City = City(number = "45", name = "Manisa", avatar = null, cityDetails = cityDetail, id = 45)
    val cities = listOf<City>(city,city,city)
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

                            composable("knowledge_details_screen"){
                                KnowledgePoolScreen(
                                    cityList = cities,
                                    navController = navController
                                )
                            }
                            composable("game_screen"){
                                GameScreen()
                            }
                            composable("setting_screen"){
                                SettingScreen()
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
                                /* ToDo: Buraya bir viewModel eklenecek.
                                    Listeden (KnowledgePool içinden tıklanan şehrin Idsini gelecek.
                                    Gelen bu idden cityViewModel.getCityWithId(cityId)) gibi bir fonksiyonla
                                    şehri alıp cityDetailsScreene gönderilecek
                                 */
                                val selectedCity = city
                                CityDetailsScreen(city = selectedCity)
                            }
                        }
                    }
                }
            }
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