package com.lml.coolweather.logic.model

data class UpdateTimeResponse(val code:String,val now :Now)
data class Now(val temp:String,val icon:String,val text : String,val windDir:String,val windScale:String,val windSpeed:String)

