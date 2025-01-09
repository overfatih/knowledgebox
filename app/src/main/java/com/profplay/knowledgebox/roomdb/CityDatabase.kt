package com.profplay.knowledgebox.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail
import com.profplay.knowledgebox.model.TypeOfCityDetail

@Database(entities = [City::class, CityDetail::class, TypeOfCityDetail::class], version = 1)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun cityDetailDao(): CityDetailDao
    abstract fun typeOfCityDetailDao(): TypeOfCityDetailDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {override fun migrate(database: SupportSQLiteDatabase) {
            /*migration yapacak olursan buradan başlayabilirsin...*/
        }}
    }

}