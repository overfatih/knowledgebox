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

class QuestionViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database"
    ).build()
    private val questionDao = db.questionTemplateDao()

    //val question = mutableStateOf<Question>(Question(questionText = "",options = listOf(),correctAnswer = ""))
    val question = mutableStateOf<Question?>(null)

    fun generateQuestion() {
        if (question.value != null) return

        viewModelScope.launch {
            val categoryCount = questionDao.getTypeOfCityDetailCount()
            val randomCategoryId = (1..(categoryCount-1)).random()
            Log.d("RandomCategoryId",randomCategoryId.toString())
            // Rastgele soru kalıbını al
            val template = questionDao.getRandomTemplateByCategory(randomCategoryId)
            val questionText: String
            val options: List<String>

            // Doğru veri al
            val cityDetail = questionDao.getRandomCityDetails(randomCategoryId, "", 1).first()
            val city = questionDao.getCityByPlateNumber(cityDetail.plateNumber)
            val incorrectCityAnswers = questionDao.getRandomCities(city.plateNumber, 3)
            val incorrectCityDetailAnswers = questionDao.getRandomCityDetails(city.plateNumber, 3)
            val isCityNameOnQuestion = questionDao.getMakeAQuestionWithTheCityName(template.templateId)
            Log.d("TemplateId",template.templateId.toString())
            val correctAnswer:Any
            Log.d("terstenSoru", isCityNameOnQuestion.toString())
            if(isCityNameOnQuestion==1){
                questionText = when (randomCategoryId) {
                    4 -> String.format(template.templateText, city.name)
                    3 -> String.format(template.templateText, city.name)
                    2 -> String.format(template.templateText, city.name)
                    1 -> String.format(template.templateText, city.name)
                    else -> ""
                }
                // Şıkları oluştur
                options = (incorrectCityDetailAnswers.map { it.feature } + cityDetail.feature).shuffled()
                correctAnswer = cityDetail.feature
            }else{
                // Soru kalıbındaki %s'leri doldur
                questionText = when (randomCategoryId) {
                    4 -> String.format(template.templateText, city.plateNumber)
                    3 -> String.format(template.templateText, cityDetail.feature)
                    2 -> String.format(template.templateText, cityDetail.feature)
                    1 -> String.format(template.templateText, cityDetail.feature)
                    else -> ""
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

