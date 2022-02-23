package com.lml.coolweather.ui.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lml.coolweather.logic.Repository
//import com.lml.coolweather.logic.model.Location
import com.lml.coolweather.logic.model.LocationResponse

class LocationViewModel : ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    val locationList=ArrayList<LocationResponse.Location>()
    val locationLiveData=Transformations.switchMap(searchLiveData){query->
        Repository.searchLocation(query)
    }
    fun searchLocation(query:String){
        searchLiveData.value=query
    }

    fun saveLocation(location:LocationResponse.Location)=Repository.saveLocation(location)
    fun getSavedLocation() = Repository.getSavedLocation()

    fun isLocationSaved() = Repository.isLocationSaved()
}