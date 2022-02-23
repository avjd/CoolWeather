package com.lml.coolweather.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CoolWeatherNetwork {
    private val weatherService=ServiceCreator2.create<WeatherService>()
    suspend fun getUpdateTimeWeather(query: String)= weatherService.getUpdateTimeWeather(query).await(query)
    suspend fun getDailyWeather(query: String)= weatherService.getDailyWeather(query).await(query)

    private val locationService=ServiceCreator.create<LocationService>()
    suspend fun searchLocation(query:String)= locationService.searchLocation(query).await(query)
    private suspend fun <T> Call<T>.await(query: String):T{
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {

                    val body=response.body()
                    Log.d("WeatherActivity",response.body().toString()+"ads"+query)
                    if (body!=null) {
                        continuation.resume(body)
                    }
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}