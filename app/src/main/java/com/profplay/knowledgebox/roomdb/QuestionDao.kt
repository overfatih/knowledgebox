package com.profplay.knowledgebox.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.QuestionTemplate

@Dao
interface QuestionDao {
    @Query("SELECT * FROM city WHERE plate_number != :excludePlateNumber ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomCities(excludePlateNumber: String, limit: Int): List<City>

    /*@Query("SELECT * FROM city_detail WHERE plate_number != :excludePlateNumber ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomCityDetails(excludePlateNumber: String, limit: Int): List<CityDetail>*/

    @Query("SELECT * FROM city_detail WHERE type_id = :typeId AND plate_number != :excludePlateNumber ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomCityDetails(typeId: Int, excludePlateNumber: String, limit: Int): List<CityDetail>

    @Query("SELECT * FROM question_template WHERE category_id = :categoryId ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTemplateByCategory(categoryId: Int): QuestionTemplate

    @Query("SELECT * FROM city WHERE plate_number = :plateNumber")
    suspend fun getCityByPlateNumber(plateNumber: String):City

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Çakışma durumunda mevcut veriyi günceller
    suspend fun insertAll(questionTemplateList: List<QuestionTemplate>):List<Long>

    @Query("SELECT COUNT(*) AS total_count FROM type_of_city_detail;  ")
    suspend fun getTypeOfCityDetailCount():Int

    @Query("SELECT make_a_question_with_the_city_name FROM question_template WHERE template_id= :templateId  ")
    suspend fun getMakeAQuestionWithTheCityName(templateId:Int):Int
}