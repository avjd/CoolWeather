package com.lml.coolweather.logic.network

import com.lml.coolweather.CoolWeatherApplication
import com.lml.coolweather.logic.model.LocationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET("v2/city/lookup?key=${CoolWeatherApplication.KEY}&range=cn")
    fun searchLocation(@Query("location") query:String):Call<LocationResponse>
}