package com.profplay.knowledgebox.view

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.roomdb.CityDatabase
import com.profplay.knowledgebox.roomdb.CityRepository
import com.profplay.knowledgebox.roomdb.MainViewModelFactory
import com.profplay.knowledgebox.screen.CityDetailsScreen
import com.profplay.knowledgebox.screen.GameScreen
import com.profplay.knowledgebox.screen.KnowledgePoolScreen
import com.profplay.knowledgebox.screen.MainScreen
import com.profplay.knowledgebox.screen.SettingScreen
import com.profplay.knowledgebox.ui.theme.KnowledgeBoxTheme
import com.profplay.knowledgebox.viewModel.MainViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    internal lateinit var myAuth: FirebaseAuth
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            CityDatabase::class.java,
            "city_database"
        ).build()
    }

    private val cityDao by lazy { database.cityDao() }
    private val cityDetailDao by lazy { database.cityDetailDao() }
    private val repository by lazy { CityRepository(cityDao, cityDetailDao) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityDetail: CityDetail = CityDetail(type = "type", feature = "detailName", plateNumber = "45", image = null)
        val  city: City = City(plateNumber = "45", name = "Manisa", avatar = null)
        val cities = listOf<City>(city,city,city)
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

                            composable("knowledge_details_screen") {
                                val cityListState = mainViewModel.cityList.collectAsState()

                                KnowledgePoolScreen(
                                    cityList = cityListState.value,
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