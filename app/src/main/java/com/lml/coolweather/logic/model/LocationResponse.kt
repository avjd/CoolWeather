package com.lml.coolweather.logic.model

data class LocationResponse(val code: String, val location: List<Location>) {
    data class Location(
        val name: String,
        val id: String,
        val lon: String,
        val lat: String,
        val adm1: String,
        val adm2: String
    )
}