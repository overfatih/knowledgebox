package com.profplay.knowledgebox.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.QuestionTemplate
import com.profplay.knowledgebox.model.TypeOfCityDetail

@Database(entities = [City::class, CityDetail::class, TypeOfCityDetail::class, QuestionTemplate::class], version = 2)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun cityDetailDao(): CityDetailDao
    abstract fun typeOfCityDetailDao(): TypeOfCityDetailDao
    abstract fun questionTemplateDao(): QuestionDao

    companion object {
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `question_template` (
                `template_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `category_id` INTEGER NOT NULL,
                `template_text` TEXT NOT NULL,
                `make_a_question_with_the_city_name` INTEGER NOT NULL
            )
        """.trimIndent())
            }
        }
    }

}