package com.profplay.knowledgebox.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.CityDetailCrossRef
import com.profplay.knowledgebox.model.CityWithDetails
import com.profplay.knowledgebox.model.CityWithType
import com.profplay.knowledgebox.model.DetailWithCities
import com.profplay.knowledgebox.model.TypeOfCityDetail

@Dao
interface CityDao {
    //CityWithDetails

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityDetailCrossRef(crossRef: CityDetailCrossRef)

    @Transaction
    @Query("SELECT * FROM city WHERE city_id = :cityId")
    suspend fun getCityWithDetails(cityId: Int): CityWithDetails


    //City

    @Query("SELECT * FROM City")
    suspend fun getAllCities(): List<City>

    @Query("SELECT * FROM city WHERE city_id= :cityId")
    fun getCityById(cityId:Int): City?

    @Query("SELECT * FROM city ORDER BY RANDOM() LIMIT 1")
    fun getRandomCity(): City?

    @Transaction
    @Query("""SELECT * FROM city WHERE city_id NOT IN (
                        SELECT city_id
                        FROM city_detail_cross_ref
                        WHERE city_detail_id = :excludeCityDetailId
                    )
                    ORDER BY RANDOM()
                    LIMIT :limit 
                    """)
    suspend fun getRandomCitiesWithOutCityDetailId(excludeCityDetailId: Int, limit: Int): List<City>

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

    //DetailWithCities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityDetail(cityDetail: CityDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityDetailCrossRef(crossRef: CityDetailCrossRef)

    @Transaction
    @Query("SELECT * FROM city_detail WHERE city_detail_id = :detailId")
    suspend fun getDetailWithCities(detailId: Int): DetailWithCities

    @Transaction
    @Query("""
        SELECT * FROM city_detail
        INNER JOIN city_detail_cross_ref ON city_detail.city_detail_id = city_detail_cross_ref.city_detail_id
        WHERE city_detail_cross_ref.city_id = :cityId AND city_detail.type_id = :typeId
    """)
    fun getDetailsByCityAndType(cityId: Int, typeId: Int): List<CityDetail>

    @Transaction
    @Query("""
        SELECT * FROM city_detail
        INNER JOIN city_detail_cross_ref ON city_detail.city_detail_id = city_detail_cross_ref.city_detail_id
        WHERE city_detail_cross_ref.city_id = :cityId
    """)
    fun getDetailsByCity(cityId: Int): List<CityDetail>

    @Transaction
    @Query("""
        SELECT * FROM city_detail
        WHERE city_detail_id NOT IN (
            SELECT city_detail_id
            FROM city_detail_cross_ref
            WHERE city_id = city_id != :cityId
        )
        AND type_id = :typeId
        ORDER BY RANDOM()
        LIMIT :limit
    """)
    fun getRandomDetailsExcludingCity(cityId: Int, typeId: Int, limit: Int): List<CityDetail>

    @Query("SELECT * FROM city_detail WHERE type_id = :typeId ORDER BY RANDOM() LIMIT :limit")
    fun getRandomDetails(typeId: Int, limit: Int): List<CityDetail>

    @Transaction
    @Query("""
    SELECT * FROM city_detail
    WHERE city_detail_id NOT IN (
        SELECT city_detail_id
        FROM city_detail_cross_ref
        WHERE city_id IN (:cityIds)
    )
    AND type_id = :typeId
    ORDER BY RANDOM()
    LIMIT :limit
""")
    fun getRandomDetailsExcludingCities(cityIds: List<Int>, typeId: Int, limit: Int): List<CityDetail>

    @Query("DELETE FROM city_detail")
    suspend fun deleteAllCityDetails()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cityDetails: List<CityDetail>): List<Long>

    @Query("SELECT * FROM city_detail")
    suspend fun getAllCityDetails(): List<CityDetail>
}

@Dao
interface TypeOfCityDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Çakışma durumunda mevcut veriyi günceller
    suspend fun insertAll(cityDetails: List<TypeOfCityDetail>):List<Long>

    @Query("SELECT * FROM type_of_city_detail")
    suspend fun getAllTypeOfCityDetails(): List<TypeOfCityDetail>

    @Query("SELECT * FROM city_detail WHERE type_id IN(:typeIds)")
    suspend fun getTypeOfCityDetailsByTypeId(typeIds: List<Int>): List<CityWithType>

    @Query("SELECT * FROM city_detail WHERE type_id IN(:typeIds) AND city_detail_id IN(:cityDetailIds)")
        suspend fun getTypeOfCityDetailsByTypeIdesAndCityDetailIdes(typeIds: List<Int>,cityDetailIds: List<Int>): List<CityWithType>

    @Query("DELETE FROM type_of_city_detail")
    suspend fun deleteAllTypeOfCityDetail()
}

@Dao
interface CityDetailCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Çakışma durumunda mevcut veriyi günceller
    suspend fun insertAll(CityDetailCrossRef: List<CityDetailCrossRef>):List<Long>

    @Query("SELECT * FROM city_detail_cross_ref")
    suspend fun getAllCityDetailCrossRef(): List<CityDetailCrossRef>

    @Query("DELETE FROM city_detail_cross_ref")
    suspend fun deleteAllCityDetailCrossRef()
}