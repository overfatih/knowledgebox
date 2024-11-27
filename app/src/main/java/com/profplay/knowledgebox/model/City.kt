package com.profplay.knowledgebox.model

data class City(val number:String, val name:String, var avatar: Int?, val cityDetails: List<CityDetail>)
data class CityDetail(var name:String,var type: String, var image: Int?)
