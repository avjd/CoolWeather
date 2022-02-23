package com.lml.coolweather.logic.network

import com.lml.coolweather.CoolWeatherApplication
import com.lml.coolweather.logic.model.DailyResponse
import com.lml.coolweather.logic.model.UpdateTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("v7/weather/now?key=${CoolWeatherApplication.KEY}")
    fun getUpdateTimeWeather(@Query("location")query: String): Call<UpdateTimeResponse>
    @GET("v7/weather/3d?key=${CoolWeatherApplication.KEY}")
    fun getDailyWeather(@Query("location")query: String):Call<DailyResponse>
}