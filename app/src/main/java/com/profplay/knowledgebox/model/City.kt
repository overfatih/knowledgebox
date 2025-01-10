package com.profplay.knowledgebox.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

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
    indices = [Index("plate_number")], // Performans için indeks ekleyin
)
data class CityDetail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "city_detail_id" )
    var cityDetailId:Int=0,

    @ColumnInfo(name = "feature" )
    var feature:String,

    @ColumnInfo(name = "type_id", defaultValue = "0")
    var typeId: Int, // TypeOfCityDetail tablosundaki typeId ile ilişkilendirilecek

    @ColumnInfo(name = "image" )
    var image: String?,

    @ColumnInfo(name = "plate_number") // Şehir plakası ile bağ
    var plateNumber: String
)

@Entity(tableName = "type_of_city_detail")
data class TypeOfCityDetail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "type_id")
    val typeId: Int,

    @ColumnInfo(name = "type_name")
    val typeName: String
)

data class CityWithType(
    @Embedded val cityDetail: CityDetail,

    @Relation(
        parentColumn = "type_id",
        entityColumn = "type_id"
    )
    val typeOfCityDetail: TypeOfCityDetail
)
