package com.lml.coolweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.lml.coolweather.CoolWeatherApplication
import com.lml.coolweather.logic.model.LocationResponse

object LocationDao {
    fun saveLocation(location: LocationResponse.Location){
        sharedPreferences().edit {
            putString("location",Gson().toJson(location))
        }
    }

    fun getSavedLocation():LocationResponse.Location{
        val locationJson= sharedPreferences().getString("location","")
        return Gson().fromJson(locationJson,LocationResponse.Location::class.java)
    }

    fun isLocationSaved()= sharedPreferences().contains("location")
    private fun sharedPreferences()=CoolWeatherApplication
        .context.getSharedPreferences("cool_weather", Context.MODE_PRIVATE)
}