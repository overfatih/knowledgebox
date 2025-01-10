package com.profplay.knowledgebox.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuestionScreen(question: Question, onNextQuestion: () -> Unit) {

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
        question?.let { q ->
            // Soru Metni
            Text(
                text = q.questionText,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            Log.d("questiontitle:", q.questionText)
            // Şıklar
            q.options.forEachIndexed { index, option ->
                val backgroundColor = when {
                    selectedOptionIndex == null -> Color.Blue // Henüz seçim yapılmadıysa
                    selectedOptionIndex == index && option == q.correctAnswer -> Color.Green // Doğru şık
                    selectedOptionIndex == index -> Color.Red // Yanlış şık
                    else -> Color.Gray // Diğer şıklar
                }
                Log.d("OPTIONS:", option)
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
        } ?: CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}