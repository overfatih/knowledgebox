package com.profplay.knowledgebox.roomdb

import android.content.Context
import android.util.Log
import com.profplay.knowledgebox.model.City
import java.io.BufferedReader
import java.io.InputStreamReader

object CityDataImporter {
    suspend fun loadCitiesFromCsv(context: Context, cityDao: CityDao) {
        val cities = mutableListOf<City>()
        val inputStream = context.assets.open("cities.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        // İlk satırı atla (Header)
        reader.readLine() // Bu satır başlığı geçmek içindir.

        reader.forEachLine { line ->
            val tokens = line.split(";")
            if (tokens.size == 4) { // id, city_name, plate_number, avatar
                val city = City(
                    cityId =  tokens[0].toInt(),
                    name = tokens[1],
                    plateNumber = tokens[2], /*This is a string*/
                    avatar = tokens[3]
                )
                cities.add(city)
                Log.d("CityImport", "Loaded city: $city")
            }
        }
        reader.close()

        cityDao.insertAll(cities)
        Log.d("CityImport", "Inserted ${cities.size} cities into Room")
    }
}