package com.profplay.knowledgebox.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun GameScreen() {
    val questions = listOf(
        Pair("What is the capital of France?", listOf("Berlin", "Madrid", "Paris", "Rome")),
        Pair("What is 2 + 2?", listOf("3", "4", "5", "6"))
    )
    val correctAnswers = listOf(2, 1) // Doğru cevapların indeksleri

    var currentQuestionIndex by remember { mutableStateOf(0) }

    if (currentQuestionIndex < questions.size) {
        val (question, options) = questions[currentQuestionIndex]
        val correctAnswerIndex = correctAnswers[currentQuestionIndex]

        QuestionScreen(
            question = question,
            options = options,
            correctAnswerIndex = correctAnswerIndex
        ) {
            currentQuestionIndex++
        }
    } else {
        // Quiz bitti ekranı
        Text(
            text = "Quiz Finished!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuestionScreen(
    question: String,
    options: List<String>,
    correctAnswerIndex: Int,
    onNextQuestion: () -> Unit
) {
    // Seçilen şık için bir state
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }

    // Column ve diğer composable'lar
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Soru Metni
        Text(
            text = question,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Şıklar
        options.forEachIndexed { index, option ->
            val backgroundColor = when {
                selectedOptionIndex == null -> Color.Blue // Henüz seçim yapılmadıysa
                selectedOptionIndex == index && index == correctAnswerIndex -> Color.Green // Doğru şık
                selectedOptionIndex == index -> Color.Red // Yanlış şık
                else -> Color.Gray // Diğer şıklar
            }

            Button(
                onClick = {
                    if (!showResult) { // Tıklanabilirlik kontrolünü bu şekilde yönetin
                        selectedOptionIndex = index
                        showResult = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }

        // 2 saniye sonra sonraki soruya geç
        if (showResult) {
            LaunchedEffect(selectedOptionIndex) {
                kotlinx.coroutines.delay(2000)
                showResult = false
                selectedOptionIndex = null
                onNextQuestion()
            }
        }
    }
}

