package com.profplay.knowledgebox.roomdb

import android.content.Context
import android.util.Log
import com.profplay.knowledgebox.model.QuestionTemplate

object QuestionTemplateDataImporter {
    suspend fun loadQuestionTemplateFromCsv(context: Context, questionDao: QuestionDao) {
        val questionTemplateList = mutableListOf<QuestionTemplate>()

        context.assets.open("question_template.csv").bufferedReader().use { reader ->
            reader.readLines().drop(1).forEach { line -> // Başlık satırını atla
                val tokens = line.split(";") // Ayırıcıyı doğru seçtiğinizden emin olun
                if (tokens.size == 4) { // Beklenen sütun sayısı id,name
                    try {
                        val questionTemplate = QuestionTemplate(
                            templateId =                    tokens[0].toInt(),
                            categoryId =                    tokens[1].toInt(),
                            makeAQuestionWithTheCityName =  tokens[2].toInt(),
                            templateText =                  tokens[3]
                        )
                        questionTemplateList.add(questionTemplate)
                    } catch (e: Exception) {
                        Log.e("QuestionTemplateList", "Error parsing line: $line", e)
                    }
                } else {
                    Log.e("questionTemplateList", "Invalid line format: $line")
                }
            }
        }

        val insertedCount = questionDao.insertAll(questionTemplateList)
        Log.d("QuestionTemplateList", "Inserted $insertedCount questionTemplate into Room")

    }
}