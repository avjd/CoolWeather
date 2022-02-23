package com.lml.coolweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class CoolWeatherApplication :Application() {
    companion object{
        const val KEY="80b823eee4c743d59c11fd0076672432"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}