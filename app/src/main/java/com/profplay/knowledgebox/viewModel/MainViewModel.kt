package com.profplay.knowledgebox.viewModel

import androidx.lifecycle.ViewModel
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.roomdb.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val repository: CityRepository): ViewModel() {
    private val _cityList = MutableStateFlow<List<City>>(emptyList())
    val cityList: StateFlow<List<City>> get() = _cityList

    fun loadAllCities() {
        // Veri yükleme işlemi
        _cityList.value = listOf(
            City(cityId = 1, name = "Ankara",plateNumber = "6",avatar = "ankara.jpeg"),
            City(cityId = 1, name = "İstanbul",plateNumber = "34",avatar = "istanbul.jpeg")
        )
    }
}