package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.AllCountryListAdapter
import com.soboft.propertybroker.adapters.AllStateListAdapter
import com.soboft.propertybroker.databinding.ActivityAddPropertyBinding
import com.soboft.propertybroker.model.Country
import com.soboft.propertybroker.model.State
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.utils.toast
import kotlinx.coroutines.*

class AddProperty : AppCompatActivity() , AllStateListAdapter.OnItemClickListner, AllCountryListAdapter.OnItemClickListener{
    private lateinit var binding: ActivityAddPropertyBinding

    private val TAG = "AddProperty"

    var PropertyTypeMasterId : Int = 9
    var AvailableForMasterId : Int = 2

    var countryId : Int = 0

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.activeButton.setOnClickListener {
            binding.activeButton.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_users_tabbar)
            binding.inactivateButton.setBackgroundColor(Color.TRANSPARENT)

        }

        binding.inactivateButton.setOnClickListener {
            binding.activeButton.setBackgroundColor(Color.TRANSPARENT)
            binding.inactivateButton.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_users_tabbar)
        }
        binding.state.setOnClickListener {
            allState()
        }

        binding.country.setOnClickListener {
            allCountry()
        }

        binding.createProperty.setOnClickListener {
//            toast("New property added successfully")
            addNewProperty()
        }

    }

    private fun allCountry() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.country_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val countryRecycler = mDialog.findViewById<RecyclerView>(R.id.CountryListRv)

        countryList(countryRecycler)
        mDialog.show()
    }

    private fun countryList(recyclerView: RecyclerView) {
        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getCountry(AppPreferences.getUserData(Params.UserId).toInt(),map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getCountryList", response.code().toString())
                        Log.d("getCountryList",response.body().toString())

                        val list : List<Country> = response.body()!!.values!!

                        recyclerView.adapter = AllCountryListAdapter(this@AddProperty,list,this@AddProperty)
                        recyclerView.layoutManager = LinearLayoutManager(this@AddProperty)

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

    private fun allState() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.state_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val stateRecycler = mDialog.findViewById<RecyclerView>(R.id.StateListRv)

        stateList(stateRecycler)
        mDialog.show()
    }

    private fun stateList(recyclerView: RecyclerView) {
        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getState(countryId,map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getStateList", response.code().toString())
                        Log.d("getStateList",response.body().toString())

                        val list : List<State> = response.body()!!.values!!

                        recyclerView.adapter = AllStateListAdapter(this@AddProperty,list,this@AddProperty)
                        recyclerView.layoutManager = LinearLayoutManager(this@AddProperty)

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

    private fun addNewProperty() {
        val propertyName = binding.propertyName.text.toString().trim()
        val Address = binding.address.text.toString().trim()
        val pincode = binding.pincode.text.toString().trim()
        val state = binding.state.text.toString().trim()
        val country = binding.country.text.toString().trim()

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()

                data["UserId"] = AppPreferences.getUserData(Params.UserId)
                data["PropertyTypeMasterId"] =PropertyTypeMasterId.toString()
                data["AvailableForMasterId"] = AvailableForMasterId.toString()
                data["PropertyName"] = propertyName
                data["PropertyAddress"] = Address
                data["PropertyPrice"] = "7999"
                data["Pincode"] = pincode
                data["CityId"] = "7"
                data["StateId"] = "7"
                data["CountryId"] = countryId.toString()
                data["IsActive"] = "1"

                val response = ServiceApi.retrofitService.addProperty(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("addNewProperty", response.code().toString())
                        Log.d("addNewProperty", response.body().toString())

                        toast("New property added successfully")
                        finish()
//                        startActivity(Intent(this@AddProperty,MyProperties::class.java))
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

    override fun onItemClick(position: Int, data: State) {
        println(position)
        println(data.countryName)

        toast("Selected" + data.state)
        binding.state.setText(data.state)
    }

    override fun onItemClick(itemPosition: Int, data: Country) {
        println(itemPosition)
        println(data.id)

        toast("Selected " + data.country)
        countryId = data.id!!.toInt()
        binding.country.setText(data.country)
    }
}