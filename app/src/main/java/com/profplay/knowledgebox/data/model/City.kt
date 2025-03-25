package com.profplay.knowledgebox.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
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

@Entity(primaryKeys = ["city_id", "city_detail_id"], tableName = "city_detail_cross_ref")
data class CityDetailCrossRef(
    val city_id: Int,
    val city_detail_id: Int
)

data class CityWithDetails(
    @Embedded val city: City,
    @Relation(
        parentColumn = "city_id",
        entityColumn = "city_detail_id",
        associateBy = Junction(CityDetailCrossRef::class)
    )
    val details: List<CityDetail>
)

data class DetailWithCities(
    @Embedded val detail: CityDetail,
    @Relation(
        parentColumn = "city_detail_id",
        entityColumn = "city_id",
        associateBy = Junction(CityDetailCrossRef::class)
    )
    val cities: List<City>
)