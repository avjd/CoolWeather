package com.lml.coolweather.ui.location

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lml.coolweather.R
import com.lml.coolweather.logic.model.LocationResponse
import com.lml.coolweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*

//import com.lml.coolweather.logic.model.Location

class LocationAdapter(private val fragment: LocationFragment,private val locationList: List<LocationResponse.Location>):
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            val locationName:TextView=view.findViewById(R.id.locationName)
            val locationAddress:TextView=view.findViewById(R.id.locationAddress)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.location_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position=holder.absoluteAdapterPosition
            val location=locationList[position]
            val activity=fragment.activity
            if (activity is WeatherActivity){
                activity.drawerLayout.closeDrawers()
                activity.viewModel.query=location.id
                activity.viewModel.placeName=location.name
                activity.refreshWeather()
            }else{
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("query",location.id)
                    putExtra("placeName",location.name)
                }

                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
            fragment.viewModel.saveLocation(location)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location=locationList[position]
        holder.locationName.text=location.name
        holder.locationAddress.text=location.adm1+" "+location.adm2
    }

    override fun getItemCount() = locationList.size


}