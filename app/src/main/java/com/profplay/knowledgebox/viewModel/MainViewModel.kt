package com.profplay.knowledgebox.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.roomdb.CityDataImporter.loadCitiesFromCsv
import com.profplay.knowledgebox.roomdb.CityDatabase
import com.profplay.knowledgebox.roomdb.CityDetailsDataImporter.loadCityDetailsFromCsv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database"
    )
        /*.fallbackToDestructiveMigration()*/ // Tüm eski verileri silip yeniden oluşturur
        .build()
    private val cityDao = db.cityDao()
    private val cityDetailDao = db.cityDetailDao()


    //val cityList = mutableStateOf<List<City>>(listOf())
    //val selectedCity = mutableStateOf<City>(City(1,"45","Manisa",""))

    init {
        /*
        * İlk çalıştırıldığında yapılacak işler için. Örneğin versiyon kontrolü yapıp firebasede daha güncel veriler varsa çekebilir.
        csv loading*/

        // CSV dosyasını yükle
        viewModelScope.launch {
            /*cityDetailDao.deleteAllCityDetails()
            Log.d("init Message","delete all citydetails")*/
            loadCitiesFromCsv(application, cityDao)
            loadCityDetailsFromCsv(application, cityDetailDao)
        }
        /*csv loading*/

    }


}