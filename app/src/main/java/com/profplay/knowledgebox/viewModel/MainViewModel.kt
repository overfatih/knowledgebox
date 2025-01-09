package com.profplay.knowledgebox.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.roomdb.CityDataImporter.loadCitiesFromCsv
import com.profplay.knowledgebox.roomdb.CityDatabase
import com.profplay.knowledgebox.roomdb.CityDetailsDataImporter.loadCityDetailsFromCsv
import com.profplay.knowledgebox.roomdb.TypeOfCityDetailDataImporter.loadTypeOfCityDetailFromCsv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    val callback = object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            db.execSQL("PRAGMA foreign_keys=ON;")  // Foreign key kısıtlamalarını etkinleştir
        }
    }

    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database",
    )
        /*.fallbackToDestructiveMigration()*/ // Tüm eski verileri silip yeniden oluşturur
        /*.addMigrations(CityDatabase.MIGRATION_1_2)*/
        .addCallback(callback)  // Callback ekliyoruz
        .build()



    private val cityDao = db.cityDao()
    private val cityDetailDao = db.cityDetailDao()
    private val typeOfCityDetailDao = db.typeOfCityDetailDao()

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
            loadTypeOfCityDetailFromCsv(application, typeOfCityDetailDao)
        }
        /*csv loading*/

    }


}