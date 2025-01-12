package com.profplay.knowledgebox.viewModel


import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.profplay.knowledgebox.model.Question
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.profplay.knowledgebox.roomdb.CityDatabase
import kotlinx.coroutines.Dispatchers

class QuestionViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database"
    ).build()
    private val questionDao = db.questionTemplateDao()
    private val cityDao = db.cityDao()
    private val cityDetailDao = db.cityDetailDao()

    val question = mutableStateOf<Question?>(null)

    fun generateQuestion() {
        if (question.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            val categoryCount = questionDao.getTypeOfCityDetailCount()
            val randomCategoryId = (1..(categoryCount)).random()
            //todo:plaka için eklediğim 4 numaralı kategoride hata var. program çöküyor.
            Log.d("RandomCategoryId",randomCategoryId.toString())
            // Rastgele soru kalıbını al
            val template = questionDao.getRandomTemplateByCategory(randomCategoryId)
            val questionText: String
            val options: List<String>

            // Doğru veri al

            val cityDetails = cityDetailDao.getRandomDetails(typeId = randomCategoryId, 1)
            if (cityDetails.isEmpty()) {
                Log.e("Error", "No city details found for typeId = $randomCategoryId")
                return@launch
            }

            val cityDetail = cityDetails.random()
            Log.d("cityDetails.random", cityDetail.cityDetailId.toString())
            val cities = cityDetailDao.getDetailWithCities(cityDetail.cityDetailId).cities
            if (cities.isEmpty()) {
                Log.e("Error", "No cities found for cityDetailId = ${cityDetail.cityDetailId}")
                return@launch
            }
            cities.forEach { Log.d("cities", it.name) }
            val cityIds = cities.map { it.cityId }
            val city = cities.random()
            val incorrectCityAnswers = cityDao.getRandomCitiesWithOutCityDetailId(cityDetail.cityDetailId, 3)
            val incorrectCityDetailAnswers = cityDetailDao.getRandomDetailsExcludingCities(cityIds = cityIds, typeId =randomCategoryId, limit= 3)
            val isCityNameOnQuestion = questionDao.getMakeAQuestionWithTheCityName(template.templateId)
            Log.d("TemplateId",template.templateId.toString())
            val correctAnswer:Any
            Log.d("terstenSoru", isCityNameOnQuestion.toString())
            if(isCityNameOnQuestion==1){
                questionText = String.format(template.templateText, city.name)
                // Şıkları oluştur
                options = (incorrectCityDetailAnswers.map { it.feature } + cityDetail.feature).shuffled()
                correctAnswer = cityDetail.feature
            }else{
                // Soru kalıbındaki %s'leri doldur
                questionText = when (randomCategoryId) {
                    4 -> String.format(template.templateText, city.plateNumber)
                    else -> String.format(template.templateText, cityDetail.feature)
                }
                // Şıkları oluştur
                options = (incorrectCityAnswers.map { it.name } + city.name).shuffled()
                correctAnswer = city.name
            }

            // Dinamik soruyu geri döndür
            question.value = Question(
                questionText = questionText,
                options = options,
                correctAnswer = correctAnswer
            )
        }
    }
}

