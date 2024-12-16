package com.profplay.knowledgebox.roomdb

import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail

class CityRepository(private val cityDao: CityDao, private val cityDetailDao: CityDetailDao) {
    fun getCityWithDetails(plateNumber: Int, type: String): Pair<City?, List<CityDetail>>  {
        val city = cityDao.getCityByPlateNumber(plateNumber)
        val details = cityDetailDao.getDetailsByPlateNumber(plateNumber, type)
        return Pair(city, details)
    }

    suspend fun getAllCities(): List<City> = cityDao.getAllCities()
}