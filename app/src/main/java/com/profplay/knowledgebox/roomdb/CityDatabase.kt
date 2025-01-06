package com.profplay.knowledgebox.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityDetail

@Database(entities = [City::class, CityDetail::class], version = 2)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun cityDetailDao(): CityDetailDao
}