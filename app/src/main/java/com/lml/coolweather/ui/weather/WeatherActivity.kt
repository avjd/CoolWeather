package com.lml.coolweather.ui.weather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lml.coolweather.R
import com.lml.coolweather.databinding.ActivityWeatherBinding
import com.lml.coolweather.logic.model.Weather
import com.lml.coolweather.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private lateinit var placeName:TextView
    private lateinit var currentTemp:TextView
    private lateinit var currentSky:TextView
    private lateinit var currentAQI:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView=window.decorView
        decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)
        placeName=findViewById(R.id.placeName)
        currentTemp=findViewById(R.id.currentTemp)
        currentSky=findViewById(R.id.currentSky)
        currentAQI=findViewById(R.id.currentAQI)
        if (viewModel.query.isEmpty()){
            viewModel.query=intent.getStringExtra("query")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("placeName")?:""
        }
        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather=result.getOrNull()
            if (weather!=null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing=false
        })
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        viewModel.refreshWeather(viewModel.query)
        navBtn.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

        })
    }
    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.query)
        swipeRefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather){
        placeName.text=viewModel.placeName
        val updateTime=weather.updateTime
        val daily=weather.daily
        val currentTempText="${updateTime.temp.toInt()} ℃"
        currentTemp.text=currentTempText
        currentSky.text= getSky(updateTime.icon).info
        val currentPM25Text="22"
        currentAQI.text=currentPM25Text
        nowLayout.setBackgroundResource(getSky(updateTime.icon).bg)
        forecastLayout.removeAllViews()
        val days=daily.size
        for (i in 0 until days) {
            val fxDate = daily[i].fxDate
            val tempMax=daily[i].tempMax
            val tempMin=daily[i].tempMin

            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            dateInfo.text = fxDate
            val sky = getSky(daily[i].iconDay)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${tempMin.toInt()} ~ ${tempMax.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        coldRiskText.text = "易发"
        dressingText.text = "多穿"
        ultravioletText.text = "良好"
        carWashingText.text = "良好"
        weatherLayout.visibility = View.VISIBLE
    }
}