package com.profplay.knowledgebox.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.CityDetailCrossRef
import com.profplay.knowledgebox.model.QuestionTemplate
import com.profplay.knowledgebox.model.TypeOfCityDetail

@Database(entities = [City::class, CityDetail::class, TypeOfCityDetail::class, QuestionTemplate::class, CityDetailCrossRef::class], version = 3)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun cityDetailDao(): CityDetailDao
    abstract fun typeOfCityDetailDao(): TypeOfCityDetailDao
    abstract fun questionTemplateDao(): QuestionDao
    abstract fun cityDetailCrossRefDao(): CityDetailCrossRefDao


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
                    """.trimIndent()
                )
            }
        }

        val migration2to3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `city_detail_cross_ref` (
                        `city_id` INTEGER NOT NULL,
                        `city_detail_id` INTEGER NOT NULL,
                        PRIMARY KEY(city_id, city_detail_id)
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE TABLE city_detail_new (
                        city_detail_id INTEGER PRIMARY KEY NOT NULL,
                        feature TEXT NOT NULL,
                        type_id INTEGER NOT NULL DEFAULT 0,
                        image TEXT
                        
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO city_detail_new (city_detail_id, feature, type_id, image)
                    SELECT city_detail_id, feature, type_id, image
                    FROM city_detail
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE city_detail")
                database.execSQL("ALTER TABLE city_detail_new RENAME TO city_detail")
            }

        }
    }

}