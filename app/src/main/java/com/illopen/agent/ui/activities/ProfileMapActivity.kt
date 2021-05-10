package com.illopen.agent.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.illopen.agent.R
import com.illopen.agent.databinding.ActivityProfileMapBinding
import com.illopen.agent.model.ProfileMapList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.PermissionUtils
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*


class ProfileMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityProfileMapBinding

    private val TAG = "ProfileMapActivity"

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1

    private lateinit var circle: Circle

    private lateinit var mapDialog : Dialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.profileMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        setupLocationPermission()
        mMap.setOnMapClickListener(OnMapClickListener { point ->
            Toast.makeText(this,point.latitude.toString() + ", " + point.longitude, Toast.LENGTH_SHORT).show()

            onMapClickPopUp(point)
        })
    }

    private fun onMapClickPopUp(point: LatLng) {
        mapDialog = Dialog(this)
        mapDialog.setContentView(R.layout.map_insert_location_popup)

        mapDialog.findViewById<TextView>(R.id.latitude).text = "Latitude : " + point.latitude.toString()
        mapDialog.findViewById<TextView>(R.id.longitude).text = "Longitude : " + point.longitude.toString()

        mapDialog.findViewById<Button>(R.id.save).setOnClickListener {

            val radius = mapDialog.findViewById<TextInputEditText>(R.id.edtRadius).text.toString().trim()

            if (radius.isNotEmpty()) {
                getUserLocation(radius,point)
                mapDialog.dismiss()
            } else {
                toast("Please enter radius")
            }
        }
        mapDialog.show()
    }

    private fun getUserLocation(radius: String, point: LatLng) {

        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["UserId"] = AppPreferences.getUserData(Params.UserId)
                map["Latitude"] = point.latitude.toString()
                map["Longitude"] = point.longitude.toString()
                map["Radius"] = radius

                val response = ServiceApi.retrofitService.getUserLocationInsert(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Insert_Location", response.code().toString())
                        Log.d("Insert_Location", response.body().toString())

                        if (response.code() == 200){
                            toast("Location Inserted Successfully")
                            startActivity(Intent(this@ProfileMapActivity,Profile::class.java))
                            finish()
                        }else{
                            toast("something wrong")
                        }

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

    private fun setupLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mapLocationAPI()

        } else
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
    }

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

    private fun mapLocationAPI() {
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
                        Log.d("profileLocation", response.code().toString())
                        Log.d("profileLocation", Gson().toJson(response.body()))

                        val mapDetailsList: List<ProfileMapList> = response.body()!!.values!!
                        mapDetailsList.forEachIndexed { index, mapDetailsList ->

                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        mapDetailsList.latitude!!.toDouble(),
                                        mapDetailsList.longitude!!.toDouble()
                                    )
                                )
                            )

                            drawCircle(
                                mapDetailsList.latitude.toDouble(),
                                mapDetailsList.longitude.toDouble(),
                                mapDetailsList.radius!!.toDouble()
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

    private fun drawCircle(latitude: Double, longitude: Double, radius: Double) {
        val circleOp = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(radius * 1609.34)
            .strokeWidth(2.0f)
            .strokeColor(Color.RED)
            .fillColor(Color.BLUE)
        circle = mMap.addCircle(circleOp)
    }
}