package com.profplay.knowledgebox.viewModel


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.profplay.knowledgebox.data.model.Question
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.profplay.knowledgebox.helper.SoundEffectManager
import com.profplay.knowledgebox.helper.TextToSpeechHelper
import com.profplay.knowledgebox.data.model.PassedQuestion
import com.profplay.knowledgebox.data.roomdb.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class QuestionViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = Room.databaseBuilder(
        getApplication(),
        CityDatabase::class.java, name = "city_database"
    ).build()
    private val questionDao = db.questionTemplateDao()
    private val cityDao = db.cityDao()
    private val cityDetailDao = db.cityDetailDao()

    private val ttsHelper: TextToSpeechHelper by lazy {
        TextToSpeechHelper(application.applicationContext)
    }

    private val soundManager: SoundEffectManager by lazy {
        SoundEffectManager(application.applicationContext)
    }

    val passedQuestionList = mutableSetOf<PassedQuestion>()
    val question = mutableStateOf<Question?>(null)
    val correctAnswers =  mutableStateOf<Int>(0)
    val totalAnswers =  mutableStateOf<Int>(0)
    val cityDetailId = mutableStateOf<Int>(0)
    val isCityNameOnQuestion = mutableStateOf<Int?>(null)
    val cityDetailImageLink = mutableStateOf<String?>("")
    //val ttsCompleted = mutableStateOf<Boolean>(true) // TTS okuma tamamlandı mı?
    private val _ttsCompleted = MutableStateFlow(false)
    val ttsCompleted: StateFlow<Boolean> get() = _ttsCompleted

    init {
        // TTS ve ses yöneticisini başlatabilirsiniz
        ttsHelper.init { }
        val optionText = question.value?.options?.joinToString(", ")
        speak("${question.value?.questionText}, Şıklar: $optionText") {}
    }

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

            withContext(Dispatchers.Main) {
                //val optionText = question.value?.options?.joinToString(", ")
                //speak("${question.value?.questionText}, Şıklar: $optionText") {}
            }

        }
    }

    suspend fun calculateScoreSuspend(selectedOptionIndex:Int?,answer:String){
        selectedOptionIndex?.let {
            //_ttsCompleted.value = false
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
                onCorrectAnswer()
            }
            else{ onWrongAnswer(answer) }
            totalAnswers.value++
            Log.d("PassedSize",passedQuestionList.size.toString())
        }
    }

    fun calculateScore(selectedOptionIndex:Int?,answer:String){
        selectedOptionIndex?.let {
            //_ttsCompleted.value = false
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
                onCorrectAnswer()
            }
            else{ onWrongAnswer(answer) }
            totalAnswers.value++
            Log.d("PassedSize",passedQuestionList.size.toString())
        }
    }

    fun onCorrectAnswer() {
        soundManager.playSound("correct")
        ttsHelper.speak("Tebrikler!"){
            _ttsCompleted.value = true
            Log.d("ttsCompletedCorrect", ttsCompleted.value.toString())
        }
    }

    fun onWrongAnswer(answer:String) {
        soundManager.playSound("wrong")
        ttsHelper.speak("Üzgünüm. Yanlış Cevap! Doğrusu: ${answer} olacaktı."){
            _ttsCompleted.value = true
            Log.d("ttsCompletedWrong", ttsCompleted.value.toString())
        }
    }

    fun speak(message: String, function: () -> Unit) {
        ttsHelper.speak(message = message){function()}
    }

    fun speakQueue(message: String) {
        ttsHelper.speakQueue(message)
    }

    fun resetTtsCompleted() {
        _ttsCompleted.value = false
        Log.d("ttsCompletedReset", ttsCompleted.value.toString())
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper.release()
        soundManager.release()
    }

    fun repeatQuestionWithSTT(context: Context, question: Question, onResult: (String) -> Unit) {
        val optionsText = question.options.joinToString(", ")
        viewModelScope.launch(Dispatchers.Main) {
            speak("Anlaşılmadı: ${question.questionText}. Şıklar: $optionsText") {

                ttsHelper.startSpeechRecognition(
                    context = context,
                    onResult = { spokenAnswer ->
                        Log.d("onResult","onResult")
                        onResult(spokenAnswer) // Kullanıcının sesli yanıtını işle

                    },
                    onError = {
                        Log.d("onError","onError")
                        // Hata durumunda yeniden deneme
                    }
                )
            }

        }
    }

    fun repeatQuestionWithTTS(question: Question) {
        val optionsText = question.options.joinToString(", ")
        viewModelScope.launch(Dispatchers.Main) {
            speak("Soruyu tekrar ediyorum: ${question.questionText}. Şıklar: $optionsText") {
            }

        }
    }


}


