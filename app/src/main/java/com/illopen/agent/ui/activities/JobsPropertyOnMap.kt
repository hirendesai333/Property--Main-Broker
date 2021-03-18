package com.illopen.agent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.databinding.ActivityJobsPropertyOnMapBinding
import com.illopen.agent.model.MapDetailsList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import kotlinx.android.synthetic.main.activity_jobs_property_on_map.*
import kotlinx.coroutines.*
import java.util.HashMap

class JobsPropertyOnMap : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "JobsPropertyOnMap"
    private lateinit var binding: ActivityJobsPropertyOnMapBinding
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)
    private lateinit var mMap: GoogleMap
    private lateinit var mapDetailsList: List<MapDetailsList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsPropertyOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getMapLocation()

    }

    private fun getMapLocation() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"
                val response = ServiceApi.retrofitService.getMapLocation(
                    AppPreferences.getUserData(Params.UserId).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, response.code().toString())
                        Log.d(TAG, Gson().toJson(response.body()))
                        mapDetailsList = response.body()!!.values!!
                        mapDetailsList.forEachIndexed { index, mapDetailsList ->
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        mapDetailsList.propertyLatitude!!.toDouble(),
                                        mapDetailsList.propertyLongitude!!.toDouble()
                                    )
                                )
                            )
                        }
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    mapDetailsList[0].propertyLatitude!!.toDouble(),
                                    mapDetailsList[0].propertyLatitude!!.toDouble()
                                )
                            )
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
    }
}