package com.profplay.knowledgebox.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.CityWithType
import com.profplay.knowledgebox.model.TypeOfCityDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query ("SELECT name, city_id, plate_number FROM city")
    fun getCityWithNameAndIdAndPlateNumber(): List<City?>

    @Query("SELECT * FROM City")
    suspend fun getAllCities(): List<City>

    @Query("SELECT * FROM city WHERE city_id= :cityId")
    fun getCityById(cityId:Int): City?

    @Query("SELECT * FROM city ORDER BY RANDOM() LIMIT 1")
    fun getRandomCity(): City?

    @Query("SELECT * FROM City WHERE plate_number = :plateNumber")
    fun getCityByPlateNumber(plateNumber: Int): City?

    @Insert
    suspend fun insert(city: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Çakışma durumunda mevcut veriyi günceller
    suspend fun insertAll(cities: List<City>)

    @Delete()
    suspend fun delete(city: City)


}

@Dao
interface CityDetailDao {
    @Query("SELECT * FROM city_detail WHERE plate_number = :plateNumber AND type_id= :typeId")
    fun getDetailsByPlateNumberAndType(plateNumber: Int, typeId: Int): CityDetail?

    @Transaction
    @Query("SELECT * FROM city_detail WHERE plate_number = :plateNumber")
    fun getDetailsByPlateNumber(plateNumber: String): List<CityWithType>

    @Query("SELECT * FROM city_detail WHERE plate_number != :plateNumber AND type_id= :typeId ORDER BY RANDOM() LIMIT :limit")
    fun getRandomDetailsExcludingPlate(plateNumber: Int, typeId:Int, limit: Int): List<CityDetail?>

    @Query("DELETE FROM city_detail")
    suspend fun deleteAllCityDetails()

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Çakışma durumunda mevcut veriyi günceller
    suspend fun insertAll(cityDetails: List<CityDetail>):List<Long>

    @Query("SELECT * FROM city_detail")
    suspend fun getAllCityDetails(): List<CityDetail>


}

@Dao
interface TypeOfCityDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Çakışma durumunda mevcut veriyi günceller
    suspend fun insertAll(cityDetails: List<TypeOfCityDetail>):List<Long>

    @Query("SELECT * FROM type_of_city_detail")
    suspend fun getAllTypeOfCityDetails(): List<TypeOfCityDetail>

    @Query("DELETE FROM type_of_city_detail")
    suspend fun deleteAllTypeOfCityDetail()
}