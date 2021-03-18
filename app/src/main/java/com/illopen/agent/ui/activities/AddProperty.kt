package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.adapters.AllCityListAdapter
import com.illopen.agent.adapters.AllCountryListAdapter
import com.illopen.agent.adapters.AllStateListAdapter
import com.illopen.agent.databinding.ActivityAddPropertyBinding
import com.illopen.agent.model.*
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*

class AddProperty : AppCompatActivity() , AllCountryListAdapter.OnItemClickListener,
    AllStateListAdapter.OnItemClickListner,
    AllCityListAdapter.OnItemClickListener{
    private lateinit var binding: ActivityAddPropertyBinding

    private val TAG = "AddProperty"

    var propertyTypeId : Int = 0
    var availableForId : Int = 0

    var countryId : Int = 0
    var stateId : Int = 0
    var cityId : Int = 0

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var countryDialog: Dialog
    private lateinit var stateDialog: Dialog
    private lateinit var cityDialog: Dialog

    private lateinit var dropDownAdapter: ArrayAdapter<String>
    private var dropdownlist : ArrayList<PropertyTypeList> = ArrayList()

    private var availablePropertyList : ArrayList<AvailablePropertyList> = ArrayList()
    private lateinit var availablePropertyAdapter : ArrayAdapter<String>

    private lateinit var from: String
    private lateinit var propertyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)

        from = intent.getStringExtra("from")!!
        propertyId = intent.getStringExtra("poropertyId")!!

        if (from == "edit") {
            // get property detail with property id
            propertyDetails()
            binding.createProperty.text = "Save"
        }

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

        binding.city.setOnClickListener {
            allCity()
        }

        binding.createProperty.setOnClickListener {
            if (from == "edit") {
                updateProperty()
            } else {
                addNewProperty()
            }
        }

        propertyTypeSpinner()
        availablePropertySpinner()
    }

    private fun propertyDetails() {

        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getPropertyDetails(
                    propertyId.toInt())

                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        val  data = response.body()!!.item!!

                        Log.d("propertyDetails", response.code().toString())
                        Log.d("propertyDetails",response.body().toString())

                        binding.propertyName.setText(data.propertyName).toString().trim()
                        binding.address.setText((data.propertyAddress)).toString().trim()
                        binding.pincode.setText(data.pincode).toString().trim()
                        binding.price.setText(data.propertyPrice).toString().trim()
                        binding.country.setText(data.countryName.toString())
                        binding.state.setText(data.stateName.toString())
                        binding.city.setText(data.cityName.toString())

                        countryId = data.countryId!!
                        stateId = data.stateId!!
                        cityId = data.cityId!!

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

    private fun availablePropertySpinner() {

        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllAvailableProperty(
                    AppPreferences.getUserData(Params.UserId).toInt(),map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("availableProperty", response.code().toString())
                        Log.d("availableProperty",response.body().toString())

                        availablePropertyList = response.body()?.values as ArrayList<AvailablePropertyList>

                        val data : MutableList<String> = ArrayList()

                        availablePropertyList.forEach {
                            data.add(it.name.toString())
                        }
                        availablePropertyAdapter = object : ArrayAdapter<String>(this@AddProperty, android.R.layout.simple_list_item_1, data) {}
                        binding.availableFor.adapter = availablePropertyAdapter

                        binding.availableFor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                availableForId = availablePropertyList[position].id!!.toInt()
//                                toast("Selected : " + availablePropertyList[position].name)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

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

    private fun propertyTypeSpinner() {

        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllPropertyType(
                    AppPreferences.getUserData(Params.UserId).toInt(),map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("PropertyType", response.code().toString())
                        Log.d("PropertyType",response.body().toString())

                        dropdownlist = response.body()?.values as ArrayList<PropertyTypeList>
                        
                        val data : MutableList<String> = ArrayList()

                        dropdownlist.forEach {
                            data.add(it.name.toString())
                        }

                        dropDownAdapter = object : ArrayAdapter<String>(this@AddProperty, android.R.layout.simple_list_item_1, data) {}
                        binding.propertyType.adapter = dropDownAdapter

                        binding.propertyType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                propertyTypeId = dropdownlist[position].id!!.toInt()
//                                toast("Selected : " + dropdownlist[position].name)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

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

    private fun allCity() {
        cityDialog = Dialog(this,R.style.Theme_PropertyMainBroker)
        cityDialog.setContentView(R.layout.city_popup)
        cityDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val cityRecycler = cityDialog.findViewById<RecyclerView>(R.id.cityListRv)

        cityList(cityRecycler)
        cityDialog.show()
    }

    private fun cityList(cityRecycler: RecyclerView) {
        coroutineScope.launch {
            try {
                val map = HashMap<String,String>()
                map["Offset"] =  "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getCity(map)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getCityList", response.code().toString())
                        Log.d("getCityList",response.body().toString())

                        val list : List<AllCity> = response.body()!!.values!!

                        cityRecycler.adapter = AllCityListAdapter(this@AddProperty,list,this@AddProperty)
                        cityRecycler.layoutManager = LinearLayoutManager(this@AddProperty)
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
        countryDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        countryDialog.setContentView(R.layout.country_popup)
        countryDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val countryRecycler = countryDialog.findViewById<RecyclerView>(R.id.CountryListRv)

        countryList(countryRecycler)
        countryDialog.show()
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
        stateDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        stateDialog.setContentView(R.layout.state_popup)
        stateDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val stateRecycler = stateDialog.findViewById<RecyclerView>(R.id.StateListRv)

        stateList(stateRecycler)
        stateDialog.show()
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
        val address = binding.address.text.toString().trim()
        val pinCode = binding.pincode.text.toString().trim()
        val price = binding.price.text.toString().trim()

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()

                data["UserId"] = AppPreferences.getUserData(Params.UserId)
                data["PropertyTypeMasterId"] = propertyTypeId.toString()
                data["AvailableForMasterId"] = availableForId.toString()
                data["PropertyName"] = propertyName
                data["PropertyAddress"] = address
                data["PropertyPrice"] = price
                data["PinCode"] = pinCode
                data["CityId"] = cityId.toString()
                data["StateId"] = stateId.toString()
                data["CountryId"] = countryId.toString()
                data["IsActive"] = "1"

                val response = ServiceApi.retrofitService.addProperty(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("addNewProperty", response.code().toString())
                        Log.d("addNewProperty", response.body().toString())

                        toast("New property added successfully")
                        startActivity(Intent(this@AddProperty,MyProperties::class.java))
                        finish()
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

    private fun updateProperty() {

        val propertyName = binding.propertyName.text.toString().trim()
        val address = binding.address.text.toString().trim()
        val pinCode = binding.pincode.text.toString().trim()
        val price = binding.price.text.toString().trim()

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()

                data["UserId"] = AppPreferences.getUserData(Params.UserId)
                data["Id"] = propertyId
                data["PropertyTypeMasterId"] = propertyTypeId.toString()
                data["AvailableForMasterId"] = availableForId.toString()
                data["PropertyName"] = propertyName
                data["PropertyAddress"] = address
                data["PropertyPrice"] = price
                data["PinCode"] = pinCode
                data["CityId"] = cityId.toString()
                data["StateId"] = stateId.toString()
                data["CountryId"] = countryId.toString()
                data["IsActive"] = "1"

                val response = ServiceApi.retrofitService.updateProperty(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("updateProperty", response.code().toString())
                        Log.d("updateProperty", response.body().toString())

                        toast("Update Property successfully")
                        startActivity(Intent(this@AddProperty,MyProperties::class.java))
                        finish()
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
        stateDialog.dismiss()
        toast("Selected" + data.state)
        stateId = data.id!!.toInt()
        binding.state.setText(data.state)
    }

    override fun onItemClick(itemPosition: Int, data: Country) {
        countryDialog.dismiss()
        toast("Selected " + data.country)
        countryId = data.id!!.toInt()
        binding.country.setText(data.country)
    }

    override fun onItemClick(itemPosition: Int, data: AllCity) {
        cityDialog.dismiss()
        toast("Selected" + data.city)
        cityId = data.id!!.toInt()
        binding.city.setText(data.city)
    }
}