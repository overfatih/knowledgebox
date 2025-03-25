package com.profplay.knowledgebox.data.roomdb

import android.content.Context
import android.util.Log
import com.profplay.knowledgebox.data.model.City
import com.profplay.knowledgebox.data.model.CityDetail
import com.profplay.knowledgebox.data.model.CityDetailCrossRef
import com.profplay.knowledgebox.data.model.TypeOfCityDetail
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

object CityDetailsDataImporter {
    suspend fun loadCityDetailsFromCsv(context: Context, cityDetailDao: CityDetailDao) {
        val cityDetails = mutableListOf<CityDetail>()

        context.assets.open("city_detail.csv").bufferedReader().use { reader ->
            reader.readLines().drop(1).forEach { line -> // Başlık satırını atla
                val tokens = line.split(";") // Ayırıcıyı doğru seçtiğinizden emin olun
                if (tokens.size == 4) { // Beklenen sütun: city_detail_id, feature, type_id, image
                    try {
                        val cityDetail = CityDetail(
                            cityDetailId = tokens[0].toInt(),
                            feature = tokens[1],
                            typeId = tokens[2].toInt(),
                            image = tokens[3]
                        )
                        cityDetails.add(cityDetail)
                    } catch (e: Exception) {
                        Log.e("CityDetails", "Error parsing line: $line", e)
                    }
                } else {
                    Log.e("CityDetails", "Invalid line format: $line")
                }
            }
        }

        val insertedCount = cityDetailDao.insertAll(cityDetails)
        Log.d("CityDetails", "Inserted $insertedCount cityDetails into Room")

    }
}

object TypeOfCityDetailDataImporter {
    suspend fun loadTypeOfCityDetailFromCsv(context: Context, typeOfCityDetailDao: TypeOfCityDetailDao) {
        val typeOfCityDetails = mutableListOf<TypeOfCityDetail>()

        context.assets.open("type_of_city_detail.csv").bufferedReader().use { reader ->
            reader.readLines().drop(1).forEach { line -> // Başlık satırını atla
                val tokens = line.split(";") // Ayırıcıyı doğru seçtiğinizden emin olun
                if (tokens.size == 2) { // Beklenen sütun sayısı id,name
                    try {
                        val typeOfCityDetail = TypeOfCityDetail(
                            typeId = tokens[0].toInt(),
                            typeName = tokens[1],
                        )
                        typeOfCityDetails.add(typeOfCityDetail)
                    } catch (e: Exception) {
                        Log.e("TypeOfCityDetails", "Error parsing line: $line", e)
                    }
                } else {
                    Log.e("TypeOfCityDetails", "Invalid line format: $line")
                }
            }
        }

        val insertedCount = typeOfCityDetailDao.insertAll(typeOfCityDetails)
        Log.d("TypeOfCityDetails", "Inserted $insertedCount typeOfCityDetails into Room")

    }
}

object CityDetailCrossRefImporter {
    suspend fun loadCityDetailCrossRefFromCsv(context: Context, cityDetailCrossRefDao: CityDetailCrossRefDao) {
        val cityDetailCrossRefList = mutableListOf<CityDetailCrossRef>()

        context.assets.open("city_detail_cross_ref.csv").bufferedReader().use { reader ->
            reader.readLines().drop(1).forEach { line -> // Başlık satırını atla
                val tokens = line.split(";") // Ayırıcıyı doğru seçtiğinizden emin olun
                if (tokens.size == 2) { // Beklenen sütun sayısı city_id,city_detail_id
                    try {
                        val cityDetailCrossRef = CityDetailCrossRef(
                            city_id = tokens[0].toInt(),
                            city_detail_id = tokens[1].toInt()
                        )
                        cityDetailCrossRefList.add(cityDetailCrossRef)
                    } catch (e: Exception) {
                        Log.e("CityDetailCrossRefList", "Error parsing line: $line", e)
                    }
                } else {
                    Log.e("CityDetailCrossRefList", "Invalid line format: $line")
                }
            }
        }

        val insertedCount = cityDetailCrossRefDao.insertAll(cityDetailCrossRefList)
        Log.d("CityDetailCrossRefList", "Inserted $insertedCount cityDetailCrossRefList into Room")

    }
}