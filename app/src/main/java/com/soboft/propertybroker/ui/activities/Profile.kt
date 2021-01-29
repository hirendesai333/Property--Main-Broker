package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.AllCountryListAdapter
import com.soboft.propertybroker.adapters.AllStateListAdapter
import com.soboft.propertybroker.adapters.AllUserLanguageAdapter
import com.soboft.propertybroker.adapters.AllUserLocationAdapter
import com.soboft.propertybroker.databinding.ActivityProfileBinding
import com.soboft.propertybroker.model.Country
import com.soboft.propertybroker.model.State
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.utils.toast
import kotlinx.coroutines.*

class Profile : AppCompatActivity(), AllCountryListAdapter.OnItemClickListener, AllStateListAdapter.OnItemClickListner {

    private lateinit var binding: ActivityProfileBinding

    private val TAG = "UserProfile"

    var countryId : Int = 0

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.save.setOnClickListener {
           updateProfile()
        }

        binding.state.setOnClickListener {
            allState()
        }

        binding.country.setOnClickListener {
            allCountry()
        }

        getUserProfile()
        getUserLanguage()
        getUserLocation()
    }

    private fun getUserLocation() {

        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getUserLocation(AppPreferences.getUserData(Params.UserId).toInt(),map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getUserLocation", response.code().toString())
                        Log.d("getUserLocation",response.body().toString())

                        val list = response.body()!!.values!!

                        binding.userLocationRv.adapter = AllUserLocationAdapter(this@Profile,list)
                        binding.userLocationRv.layoutManager = LinearLayoutManager(this@Profile,LinearLayoutManager.HORIZONTAL,false)
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

    private fun getUserLanguage() {

        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getUserLanguage(AppPreferences.getUserData(Params.UserId).toInt(),map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getUserLanguages", response.code().toString())
                        Log.d("getUserLanguages",response.body().toString())

                        val list = response.body()!!.values!!

                        binding.userLanguageRv.adapter = AllUserLanguageAdapter(this@Profile,list)
                        binding.userLanguageRv.layoutManager = LinearLayoutManager(this@Profile,LinearLayoutManager.HORIZONTAL,false)
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

                        recyclerView.adapter = AllStateListAdapter(this@Profile,list,this@Profile)
                        recyclerView.layoutManager = LinearLayoutManager(this@Profile)

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

                        recyclerView.adapter = AllCountryListAdapter(this@Profile,list,this@Profile)
                        recyclerView.layoutManager = LinearLayoutManager(this@Profile)

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

    private fun updateProfile() {

        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val companyName = binding.companyName.text.toString().trim()
        val address = binding.address.text.toString().trim()

        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()

                map["UserId"] = AppPreferences.getUserData(Params.UserId)
                map["UserTypeMasterId"] =  AppPreferences.getUserData(Params.UserTypeMasterId)
                map["FirstName"] = firstName
                map["LastName"] = lastName
                map["CompanyName"] = companyName
                map["Email"] = email
                map["CountryId"] = countryId.toString()
                map["Address"] = address

                val response = ServiceApi.retrofitService.updateUserProfile(map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("updateProfile", response.code().toString())
                        Log.d("updateProfile",response.body().toString())

                        toast("Update Profile Successful..")
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

    private fun getUserProfile() {

        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getUserProfile(
                    AppPreferences.getUserData(Params.UserId).toInt()
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getUserProfile", response.code().toString())
                        Log.d("getUserProfile",response.body().toString())

                        val response = response.body()!!.item!!

                        binding.profileImage.load("http://realestateapi.lamproskids.com/" + response.profileUrl)
                        binding.firstName.setText(response.firstName).toString().trim()
                        binding.lastName.setText(response.lastName).toString().trim()
                        binding.email.setText(response.email).toString().trim()
                        binding.companyName.setText(response.companyName).toString().trim()
                        binding.address.setText(response.address.toString().trim())
                        binding.country.setText(response.countryName).toString().trim()
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

    override fun onItemClick(itemPosition: Int, data: Country) {

        println(itemPosition)
        println(data.id)

        toast("Selected " + data.country)
        countryId = data.id!!.toInt()
        binding.country.setText(data.country)


    }

    override fun onItemClick(position: Int, data: State) {
        println(position)
        println(data.countryName)

        toast("Selected" + data.state)
        binding.state.setText(data.state)
    }
}