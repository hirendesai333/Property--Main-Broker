package com.illopen.agent.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.adapters.AllMyPostedJobsAdapter
import com.illopen.agent.adapters.MapDetailsPropertyAdapter
import com.illopen.agent.databinding.ActivityJobsPropertyOnMapBinding
import com.illopen.agent.model.MyPostedJobList
import com.illopen.agent.model.ProfileMapList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.PermissionUtils
import kotlinx.coroutines.*
import java.util.HashMap

class JobsPropertyOnMap : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "JobsPropertyOnMap"

    private lateinit var binding: ActivityJobsPropertyOnMapBinding

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var mMap: GoogleMap
    private lateinit var mapDetailsList: List<ProfileMapList>

    private val LOCATION_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsPropertyOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getPropertyDetails()
    }

    private fun getPropertyDetails() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllJobLang(map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val list = response.body()!!.values!!
                        binding.details.adapter = MapDetailsPropertyAdapter(this@JobsPropertyOnMap,list)
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
                        Log.d("JobPropertyLocation", response.code().toString())
                        Log.d("JobPropertyLocation", Gson().toJson(response.body()))
                        mapDetailsList = response.body()!!.values!!
                        mapDetailsList.forEachIndexed { index, mapDetailsList ->
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        mapDetailsList.latitude!!.toDouble(),
                                        mapDetailsList.longitude!!.toDouble()
                                    )
                                )
                            )
                        }
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    mapDetailsList[0].latitude!!.toDouble(),
                                    mapDetailsList[0].longitude!!.toDouble()
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
        getLocationAccess()
    }

    private fun getLocationAccess() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            getMapLocation()
        } else
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
    }

//    @SuppressLint("MissingPermission")
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    when {
//                        PermissionUtils.isLocationEnabled(this) -> {
//                            setUpLocationListener()
//                        }
//                        else -> {
//                            PermissionUtils.showGPSNotEnabledDialog(this)
//                        }
//                    }
//                } else {
//                    Toast.makeText(
//                        this, "LOCATION_PERMISSION_REQUEST", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this, LOCATION_PERMISSION_REQUEST
                )
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
//                        latTextView.text = location.latitude.toString()
//                        lngTextView.text = location.longitude.toString()
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()!!
        )
    }
}