package com.profplay.knowledgebox.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.CityWithType
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
    private val typeOfCityDetailDao = db.typeOfCityDetailDao()


    val selectedCity = mutableStateOf<City>(City(45,"45","Manisa",""))
    val selectedCityDetails = mutableStateOf<List<CityDetail>>(listOf())
    val selectedDetailsByCity = mutableStateOf<List<CityDetail>>(listOf())
    val selectedCityWithDetails = mutableStateOf<List<CityWithType>>(listOf())

    fun getCity(cityId:Int){
        viewModelScope.launch(Dispatchers.IO){
            val city = cityDao.getCityById(cityId)
            city?.let {
                selectedCity.value = it
            }
        }
    }

    fun getCityDetails(cityId: Int){
        selectedCityDetails.value = emptyList()
        viewModelScope.launch(Dispatchers.IO){
            val cityDetailsList = cityDao.getCityWithDetails(cityId).details
            cityDetailsList?.let() {
                selectedCityDetails.value = it
            }
        }
    }

    fun getDetailsByCity(cityId: Int){
        selectedDetailsByCity.value = emptyList()
        viewModelScope.launch(Dispatchers.IO){
            val cityDetailsList = cityDetailDao.getDetailsByCity(cityId)
            cityDetailsList?.let() {
                selectedDetailsByCity.value = it
            }
        }
    }
    fun getCityWithDetails(cityId: Int){
        viewModelScope.launch(Dispatchers.IO){
            val cityWithDetails = cityDao.getCityWithDetails(cityId)
            cityWithDetails?.let() {
                val typeIds = it.details.map {it.typeId }
                val cityDetailIds = it.details.map {it.cityDetailId }
                selectedCityWithDetails.value = typeOfCityDetailDao.getTypeOfCityDetailsByTypeIdesAndCityDetailIdes(typeIds,cityDetailIds)
            }
                    }
    }


}