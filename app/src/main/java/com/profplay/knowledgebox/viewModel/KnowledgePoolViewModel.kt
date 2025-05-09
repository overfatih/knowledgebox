package com.profplay.knowledgebox.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.profplay.knowledgebox.data.model.City
import com.profplay.knowledgebox.data.roomdb.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KnowledgePoolViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database"
    ).build()
    private val cityDao = db.cityDao()

    val cityList = mutableStateOf<List<City>>(listOf())

    fun getAllCities() {
        viewModelScope.launch (Dispatchers.IO) {
            cityList.value = cityDao.getAllCities()
        }
    }
}