package com.profplay.knowledgebox.viewModel


import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.profplay.knowledgebox.model.Question
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.profplay.knowledgebox.model.PassedQuestion
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

    val passedQuestionList = mutableSetOf<PassedQuestion>()
    val question = mutableStateOf<Question?>(null)
    val correctAnswers =  mutableStateOf<Int>(0)
    val totalAnswers =  mutableStateOf<Int>(0)
    val cityDetailId = mutableStateOf<Int>(0)
    val isCityNameOnQuestion = mutableStateOf<Int?>(null)
    val cityDetailImageLink = mutableStateOf<String?>("")

    fun generateQuestion() {
        if (question.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            val categoryCount = questionDao.getTypeOfCityDetailCount()
            val randomCategoryId = (1..(categoryCount)).random()
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
            cityDetailId.value = cityDetail.cityDetailId
            Log.d("cityDetails.random", cityDetail.cityDetailId.toString())
            isCityNameOnQuestion.value = questionDao.getMakeAQuestionWithTheCityName(template.templateId)
            Log.d("TemplateId",template.templateId.toString())
            Log.d("terstenSoru", isCityNameOnQuestion.value.toString())

            // Sorunun daha önce çıkıp çıkmadığını kontrol et
            if (passedQuestionList.contains(PassedQuestion(cityDetail.cityDetailId, isCityNameOnQuestion.value!!))) {
                generateQuestion() // Tekrar yeni bir soru oluştur
                return@launch
            }
            cityDetailImageLink.value = cityDetail.image
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
            val correctAnswer:Any
            if(isCityNameOnQuestion.value==1){
                questionText = String.format(template.templateText, city.name) // Soru kalıbındaki %s'leri doldur
                options = (incorrectCityDetailAnswers.map { it.feature } + cityDetail.feature).shuffled() // Şıkları oluştur
                correctAnswer = cityDetail.feature
            }else{
                questionText = String.format(template.templateText, cityDetail.feature) // Soru kalıbındaki %s'leri doldur
                options = (incorrectCityAnswers.map { it.name } + city.name).shuffled() // Şıkları oluştur
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

    fun calculateScore(selectedOptionIndex:Int?){
        selectedOptionIndex?.let {
            val selectedAnswer = question.value?.let { it.options[selectedOptionIndex] }?:""
            if (selectedAnswer == question.value?.correctAnswer) {
                correctAnswers.value++
                // Soru bilgilerini geçmiş sorular listesine ekle
                question.value?.let {
                    passedQuestionList.add(
                        PassedQuestion(
                            cityDetailId = cityDetailId.value,
                            isCityNameOnQuestion = isCityNameOnQuestion.value!!
                        )
                    )
                }
            }
            totalAnswers.value++
            Log.d("PassedSize",passedQuestionList.size.toString())
        }
    }
}

