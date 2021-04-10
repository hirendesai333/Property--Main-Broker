package com.illopen.agent.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.illopen.agent.adapters.AllJobLanguagesAdapter
import com.illopen.agent.adapters.PropertyMoreDetailsAdapter
import com.illopen.agent.databinding.ActivityPropertyMoreDetailsBinding
import com.illopen.agent.model.PropertyMoreDetailsList
import com.illopen.agent.network.ServiceApi
import kotlinx.coroutines.*

class PropertyMoreDetails : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyMoreDetailsBinding

    private val TAG = "PropertyMoreDetails"

    lateinit var propertyName : String

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

        propertyName = intent.getStringExtra("propertyName")!!
        binding.title.text = propertyName

        propertyMoreDetailsAPI()
    }

    private fun propertyMoreDetailsAPI() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.propertyMoreDetails(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("PropertyMoreDetails", response.code().toString())
                        Log.d("PropertyMoreDetails", response.body().toString())

                        val list : List<PropertyMoreDetailsList> = response.body()!!.values!!

                        binding.moreDetails.adapter = PropertyMoreDetailsAdapter(this@PropertyMoreDetails, list)
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