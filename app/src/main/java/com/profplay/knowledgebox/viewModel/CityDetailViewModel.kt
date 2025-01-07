package com.profplay.knowledgebox.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.roomdb.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityDetailViewModel(application: Application): AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database"
    ).build()
    private val cityDao = db.cityDao()
    private val cityDetailDao = db.cityDetailDao()

    val selectedCity = mutableStateOf<City>(City(45,"45","Manisa",""))
    val selectedCityDetails = mutableStateOf<List<CityDetail?>>(listOf())

    fun getCity(cityId:Int){
        viewModelScope.launch(Dispatchers.IO){
            val city = cityDao.getCityById(cityId)
            city?.let {
                selectedCity.value = it
            }
        }
    }

    fun getCityDetails(plateNumber: String){
        selectedCityDetails.value = emptyList()
        viewModelScope.launch(Dispatchers.IO){
            val cityDetailsList = cityDetailDao.getDetailsByPlateNumber(plateNumber)
            cityDetailsList?.let {
                selectedCityDetails.value = it
            }
        }
    }


}