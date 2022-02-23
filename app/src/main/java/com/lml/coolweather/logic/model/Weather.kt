package com.lml.coolweather.logic.model

data class Weather(val updateTime:Now, val daily: List<DailyResponse.Daily>)
