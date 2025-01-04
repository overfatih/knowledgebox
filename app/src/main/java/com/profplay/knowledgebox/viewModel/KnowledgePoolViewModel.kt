package com.profplay.knowledgebox.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.roomdb.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class KnowledgePoolViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "Cities"
    ).build()
    private val cityDao = db.cityDao()

    val cityList = mutableStateOf<List<City>>(listOf())

    fun getAllCities() {
        viewModelScope.launch (Dispatchers.IO) {
            cityList.value = cityDao.getAllCities()
        }
        /*
        // Veri yükleme işlemi
        _cityList.value = listOf(
            City(cityId = 1, name = "Ankara",plateNumber = "6",avatar = "ankara.jpeg"),
            City(cityId = 1, name = "İstanbul",plateNumber = "34",avatar = "istanbul.jpeg")
        )
        */
    }
}