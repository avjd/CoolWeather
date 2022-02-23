package com.lml.coolweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.lml.coolweather.logic.dao.LocationDao
import com.lml.coolweather.logic.model.LocationResponse
import com.lml.coolweather.logic.model.Weather
//import com.lml.coolweather.logic.model.Location
import com.lml.coolweather.logic.network.CoolWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException

object Repository {
    fun searchLocation(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val locationResponse = CoolWeatherNetwork.searchLocation(query)
            if (locationResponse.code == "200") {
                val location = locationResponse.location
//                Log.d("Repository",locationResponse.location.toString()+"111")
                Result.success(location)
            } else {
                Result.failure(RuntimeException("response code is ${locationResponse.code}"))
            }
        } catch (e: Exception) {
            Result.failure<List<LocationResponse.Location>>(e)
        }
        emit(result)
    }

    fun refreshWeather(query: String)= liveData(Dispatchers.IO) {
        val result=try {
            coroutineScope {
                Log.d("WeatherActivity",query+"ads")

                val deferredUpdateTime=async { CoolWeatherNetwork.getUpdateTimeWeather(query) }
                val deferredDaily=async { CoolWeatherNetwork.getDailyWeather(query) }
                val updateTimeResponse=deferredUpdateTime.await()
                val dailyResponse=deferredDaily.await()
                if (updateTimeResponse.code=="200"&&dailyResponse.code=="200"){
                    val weather=Weather(updateTimeResponse.now, dailyResponse.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "updateTimeResponse response code is ${updateTimeResponse.code}" +
                                    "daily response code is ${dailyResponse.code}"
                        )
                    )
                }
            }
        }catch (e:Exception){
            Result.failure<Weather>(e)
        }
        emit(result)
    }

    fun saveLocation(location: LocationResponse.Location) = LocationDao.saveLocation(location)

    fun getSavedLocation() = LocationDao.getSavedLocation()

    fun isLocationSaved() = LocationDao.isLocationSaved()
//    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
//        liveData<Result<T>>(context) {
//            val result = try {
//                block
//            } catch (e: Exception) {
//                Result.failure<T>(e)
//            }
//            emit(result as Result<T>)
//        }
}