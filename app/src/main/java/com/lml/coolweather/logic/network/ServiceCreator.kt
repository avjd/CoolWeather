package com.lml.coolweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL="https://geoapi.qweather.com/"
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass:Class<T>):T= retrofit.create(serviceClass)
    inline fun <reified T> create():T= create(T::class.java)
}

object ServiceCreator2 {
    private const val BASE_URL="https://devapi.qweather.com/"
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass:Class<T>):T= retrofit.create(serviceClass)
    inline fun <reified T> create():T= create(T::class.java)
}