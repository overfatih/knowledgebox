package com.profplay.knowledgebox.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Question(
    val questionText: String, // Oluşturulan soru
    val options: List<String>, // Şıklar
    val correctAnswer: String // Doğru cevap
)

@Entity(tableName = "question_template")
data class QuestionTemplate(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "template_id")
    val templateId: Int = 0,

    @ColumnInfo(name = "category_id") // TypeOfCityDetail ile ilişkilendirme
    val categoryId: Int,

    @ColumnInfo(name = "template_text") // Kalıp: "%s plakalı şehir aşağıdakilerden hangisidir?"
    val templateText: String,

    @ColumnInfo(name= "make_a_question_with_the_city_name")
    val makeAQuestionWithTheCityName: Int
)
