package com.profplay.knowledgebox.screen

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.profplay.knowledgebox.data.model.Question
import com.profplay.knowledgebox.viewModel.QuestionViewModel

@Composable
fun QuestionWithTTSScreen(question: Question,
                          correctAnswers:  Int,
                          totalAnswers:  Int,
                          cityDetailImageLink: String?,
                          questionViewModel: QuestionViewModel,
                          onNextQuestion: (selectedOptionIndex: Int) -> Unit) {

    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var isRetrying by remember { mutableStateOf(false) } // Tekrar okuma durumu (ToDo:bunu bir tuşa bağla)

    var ttsCompleted by remember { mutableStateOf(false) } // TTS okuma tamamlandı mı?

    LaunchedEffect(Unit) {
        val optionsText = question.options.joinToString(", ")
        questionViewModel.speak("${question.questionText}, Şıklar: $optionsText") {
            ttsCompleted = true // TTS tamamlandığında güncelle
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
                    option == q.correctAnswer -> Color.Green
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
                ttsCompleted = true // TTS tamamlandığında güncelle
                // Soruyu tekrar okuma
                LaunchedEffect(Unit) {
                    questionViewModel.repeatQuestionWithTTS(question)
                }
                isRetrying = false
            }

            if (showResult) {
                Log.d("selectedOptionIndex", selectedOptionIndex.toString())
                Log.d("Scores", "${totalAnswers}:${correctAnswers}")

                selectedOptionIndex?.let {
                    onNextQuestion(it)
                }
                showResult = false
            }

        } ?: CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        // Progress Bar
        Spacer(modifier = Modifier.height(24.dp))
        ProgressBarTTS(correctAnswers = correctAnswers, totalAnswers = totalAnswers)
    }
}

@Composable
fun ProgressBarTTS(correctAnswers: Int, totalAnswers: Int) {
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
