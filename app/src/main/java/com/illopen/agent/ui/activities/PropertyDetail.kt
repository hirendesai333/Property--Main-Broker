package com.illopen.agent.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.illopen.agent.adapters.PropertyDetailsAmenitiesAdapter
import com.illopen.agent.adapters.ProperyImagesListAdapter
import com.illopen.agent.databinding.ActivityPropertyDetailBinding
import com.illopen.agent.model.PropertyImageList
import com.illopen.agent.model.PropertyListModel
import com.illopen.agent.model.PropertyMoreDetailsList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.Params
import kotlinx.coroutines.*
import java.util.ArrayList

class PropertyDetail : AppCompatActivity() {

    private val TAG = "PropertyDetail"
    private lateinit var binding: ActivityPropertyDetailBinding

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var propertyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        propertyId = intent.getStringExtra("propertyMasterId")!!

        getJobPropertyDetails()

        propertyDetailsAmenities()
        propertyDetailImage()
    }

    private fun propertyDetailsAmenities() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.propertyMoreDetailsAll(
                    propertyId.toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("propertyAmenities", response.code().toString())
                        Log.d("propertyAmenities", response.body().toString())

                        if (response.code() == 200) {

                            val list: List<PropertyMoreDetailsList> = response.body()!!.values!!
                            binding.amenitiesRv.adapter =
                                PropertyDetailsAmenitiesAdapter(this@PropertyDetail, list)

                        } else {

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

    private fun propertyDetailImage() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.propertyImageAll(
                    propertyId.toInt(),map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("propertyDetailImage", response.code().toString())
                        Log.d("propertyDetailImage",response.body().toString())

//                        val list : List<PropertyImageList> = response.body()!!.values!!
//                        binding.amenitiesRv.adapter = PropertyDetailsAmenitiesAdapter(this@PropertyDetail,list)
//                        binding.propertyBanner.load(response.body()!!.values!![0].url)

                        val sliderModel = ArrayList<SlideModel>()
                        val imageList: List<PropertyImageList> = response.body()!!.values!!

                        for (i in imageList.indices) {
                            sliderModel.add(SlideModel(imageList[i].urlStr, ScaleTypes.FIT))
                            binding.imageSlider.setImageList(sliderModel)
                        }

                    }
                }else{
                    withContext(Dispatchers.Main){
                        Log.d(TAG, "something wrong")
                    }
                }
            }catch (e : Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun getJobPropertyDetails() {
        coroutineScope.launch {
            try {

                val response = ServiceApi.retrofitService.getJobPropertyDetails(propertyId.toInt())
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getJobPropertyDetail", response.code().toString())
                        Log.d("getJobPropertyDetail", response.body().toString())

//                        val list : String = response.body()!!.item!!.propertyDetailMasterName.toString()

//                        binding.myPostedJobRv.adapter = MyPostedJobDetailsAdapter(this@MyPostedJobDetails,list,this@MyPostedJobDetails)
//                        binding.title.text = response.body()!!.item!!.propertyName.toString()

                        binding.clientName.text = response.body()!!.item!!.propertyName.toString()
                        binding.propertyType.text = response.body()!!.item!!.propertyType.toString()
                        binding.availableFor.text = response.body()!!.item!!.availableFor.toString()
                        binding.price.text = response.body()!!.item!!.propertyPrice.toString()
                        binding.notes.text = response.body()!!.item!!.propertyNotes.toString()
                        binding.address.text = response.body()!!.item!!.propertyAddress.toString()
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
}