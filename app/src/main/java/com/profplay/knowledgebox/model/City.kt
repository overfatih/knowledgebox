package com.profplay.knowledgebox.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "city",
    indices = [Index(value = ["plate_number"], unique = true)] // Benzersiz plaka numarası
)
data class City(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "city_id")
    var cityId: Int = 0,

    @ColumnInfo(name="plate_number")
    val plateNumber: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "avatar")
    var avatar: String?
)

@Entity(
    tableName = "city_detail",
    indices = [Index("plate_number")] // Performans için indeks ekleyin
)
data class CityDetail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "city_detail_id" )
    var cityDetailId:Int=0,

    @ColumnInfo(name = "feature" )
    var feature:String,

    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "image" )
    var image: String?,

    @ColumnInfo(name = "plate_number") // Şehir plakası ile bağ
    var plateNumber: String
)
