package com.lml.coolweather.ui.weather

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lml.coolweather.logic.Repository

class WeatherViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<String>()
    var placeName=""
    var query=""
    val weatherLiveData=Transformations.switchMap(locationLiveData){query->
        Repository.refreshWeather(query)
    }
    fun refreshWeather(query:String){
        locationLiveData.value=query
    }
}