package com.lml.coolweather.ui.location

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lml.coolweather.MainActivity
import com.lml.coolweather.R
import com.lml.coolweather.databinding.FragmentLocationBinding
import com.lml.coolweather.ui.weather.WeatherActivity

class LocationFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(LocationViewModel::class.java) }
    private lateinit var adapter: LocationAdapter
    private var _binding: FragmentLocationBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_location,container,false)
        _binding= FragmentLocationBinding.bind(view)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
        adapter= LocationAdapter(this,viewModel.locationList)
        if (activity is MainActivity&&viewModel.isLocationSaved()){
            val location=viewModel.getSavedLocation()
            val intent=Intent(context,WeatherActivity::class.java).apply {
                putExtra("query",location.id)
                putExtra("placeName",location.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        binding.recyclerView.adapter=adapter
        binding.searchLocationEdit.addTextChangedListener { editable->
            val content=editable.toString()
            if(content.isNotEmpty()){
                viewModel.searchLocation(content)
            }else{
                binding.recyclerView.visibility=View.GONE
                binding.bgImageView.visibility=View.VISIBLE
                viewModel.locationList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.locationLiveData.observe(this, Observer { result->
            val locations=result.getOrNull()
            if(locations!=null){
                binding.recyclerView.visibility=View.VISIBLE
                binding.bgImageView.visibility=View.GONE
                viewModel.locationList.clear()
                viewModel.locationList.addAll(locations)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}