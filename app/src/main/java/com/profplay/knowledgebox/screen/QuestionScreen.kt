package com.profplay.knowledgebox.screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.profplay.knowledgebox.model.Question
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.profplay.knowledgebox.viewModel.QuestionViewModel
import kotlinx.coroutines.launch

@Composable
fun QuestionScreen(question: Question,
                   correctAnswers:  Int,
                   totalAnswers:  Int,
                   cityDetailImageLink: String?,
                   questionViewModel: QuestionViewModel,
                   onNextQuestion: (selectedOptionIndex: Int) -> Unit) {

    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var isRetrying by remember { mutableStateOf(false) } // Tekrar okuma durumu

    var isListening by remember { mutableStateOf(false) } // Mikrofon durumu
    var ttsCompleted by remember { mutableStateOf(false) } // TTS okuma tamamlandı mı?

    val context = LocalContext.current


    // Kullanıcının sesli yanıtını işleme
    fun handleSpokenAnswer(spokenAnswer: String) {

        val normalizedAnswer = spokenAnswer.replace("\\s+".toRegex(), "").trim().lowercase()
        val correctAnswer = question.correctAnswer.replace("\\s+".toRegex(), "").trim().lowercase()
        //1 -> tam eşleşme        2 -> kapsıyor mu?        3 -> %80'den fazla benzerlik
        val matchedIndex = question.options.filter { option ->
            option.replace("\\s+".toRegex(), "").trim().lowercase() == normalizedAnswer ||
                    normalizedAnswer.contains(
                        option.replace("\\s+".toRegex(), "").trim().lowercase()
                    ) ||
                    calculateSimilarity(
                        normalizedAnswer,
                        (option.replace("\\s+".toRegex(), "").trim().lowercase())
                    ) >= 70
        }.firstOrNull()?.let { question.options.indexOf(it) } ?: -1
        if (matchedIndex != -1) {
            selectedOptionIndex = matchedIndex
            showResult = true
            val isCorrect = when {
                correctAnswer == normalizedAnswer -> matchedIndex
                normalizedAnswer.contains(correctAnswer) -> matchedIndex
                calculateSimilarity(normalizedAnswer, correctAnswer) >= 70 -> matchedIndex
                else -> -1
            }
            if (isCorrect != -1) {
                Log.d("AnswerCheck", "Correct Answer!"+normalizedAnswer+" = "+correctAnswer)
            } else {
                Log.d("AnswerCheck", "Wrong Answer!"+normalizedAnswer+" != "+correctAnswer)
            }
        } else {
            Log.d("AnswerCheck", "No match found for spoken answer."+normalizedAnswer+" !! "+correctAnswer)
            isRetrying = true
            showResult = false
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()

            if (!spokenText.isNullOrEmpty()) {
                handleSpokenAnswer(spokenText) // Gelen cevabı işleme fonksiyonuna gönderiyoruz.
                isListening = false // Mikrofonu kapat
            }
        }
    }

    LaunchedEffect(Unit) {
        val optionsText = question.options.joinToString(", ")
        questionViewModel.speak("${question.questionText}, Şıklar: $optionsText") {
            ttsCompleted = true // TTS tamamlandığında güncelle
        }
    }

    // Mikrofonu TTS tamamlandığında otomatik olarak başlat
    LaunchedEffect(ttsCompleted) {
        if (ttsCompleted && !isListening) {
            Log.d("ttsCompleted", ttsCompleted.toString())
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Cevabınızı söyleyin")
            }
            try {
                launcher.launch(intent)
                isListening = true // Mikrofon aktif
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "STT desteklenmiyor", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Question Text and Options
        Log.d("cityDetailImageLink", cityDetailImageLink ?: "")
        question.let { q ->
            cityDetailImageLink?.let { avatar ->
                val drawableUri =
                    "android.resource://${LocalContext.current.packageName}/drawable/${avatar}"
                AsyncImage(
                    model = drawableUri,
                    contentDescription = "Soruya ait resim.",
                    modifier = Modifier.padding(8.dp, 16.dp).fillMaxWidth().height(200.dp)
                )
            }
            Text(
                text = q.questionText,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            q.options.forEachIndexed { index, option ->
                val backgroundColor = when {
                    selectedOptionIndex == null -> Color.Blue
                    selectedOptionIndex == index && option == q.correctAnswer -> Color.Green
                    selectedOptionIndex == index && option != q.correctAnswer -> Color.Red
                    option == q.correctAnswer -> Color(red = 255, green = 165, blue = 0)
                    else -> Color.Gray
                }
                Button(
                    onClick = {
                        if (!showResult) {
                            selectedOptionIndex = index
                            showResult = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            if (isRetrying) {
                isListening = true
                ttsCompleted = true // TTS tamamlandığında güncelle
                // Soruyu tekrar okuma ve mikrofonu açma
                LaunchedEffect(Unit) {
                    questionViewModel.repeatQuestionWithSTT(context, question) { spokenAnswer ->
                        handleSpokenAnswer(spokenAnswer)
                    }
                }
                isRetrying = false
            }

            if (showResult) {
                Log.d("selectedOptionIndex", selectedOptionIndex.toString())
                Log.d("Scores", "${totalAnswers}:${correctAnswers}")
                showResult = false
                selectedOptionIndex?.let {
                    onNextQuestion(it)
                }
            }

        } ?: CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        // Progress Bar
        Spacer(modifier = Modifier.height(24.dp))
        ProgressBar(correctAnswers = correctAnswers, totalAnswers = totalAnswers)
    }
}

@Composable
fun ProgressBar(correctAnswers: Int, totalAnswers: Int) {
    val progress = if (totalAnswers > 0) correctAnswers / totalAnswers.toFloat() else 0.5f
    val incorrectProgress = 1f - progress

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Gray)
    ) {
        // Doğru cevaplar için yeşil bar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(progress.coerceAtLeast(0.01f))
                .background(Color.Green)
        )

        // Yanlış cevaplar için kırmızı bar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(incorrectProgress.coerceAtLeast(0.01f))
                .background(Color.Red)
        )
    }
}

// Benzerlik oranını hesaplayan fonksiyon (örnek olarak editDistance kullanılıyor)
fun calculateSimilarity(answer: String, correctAnswer: String): Int {
    val distance = getEditDistance(answer, correctAnswer)
    val maxLength = maxOf(answer.length, correctAnswer.length)
    return if (maxLength == 0) 100 else ((1 - distance.toDouble() / maxLength) * 100).toInt()
}

// Levenshtein mesafesini hesaplayan fonksiyon
fun getEditDistance(a: String, b: String): Int {
    val dp = Array(a.length + 1) { IntArray(b.length + 1) }

    for (i in 0..a.length) dp[i][0] = i
    for (j in 0..b.length) dp[0][j] = j

    for (i in 1..a.length) {
        for (j in 1..b.length) {
            dp[i][j] = if (a[i - 1] == b[j - 1]) {
                dp[i - 1][j - 1]
            } else {
                minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]) + 1
            }
        }
    }
    return dp[a.length][b.length]
}

