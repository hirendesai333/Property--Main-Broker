package com.illopen.agent.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.illopen.agent.adapters.AllJobLanguagesAdapter
import com.illopen.agent.databinding.ActivityProfileBinding
import com.illopen.agent.model.AllJobLanguageList
import com.illopen.agent.model.Country
import com.illopen.agent.model.PropertyTypeList
import com.illopen.agent.model.SelectedLanguageModel
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.MediaLoader
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.regex.Pattern

class Profile : AppCompatActivity(){

    private lateinit var binding: ActivityProfileBinding

    private val TAG = "UserProfile"

    var countryId: Int = 0

    private lateinit var progressDialog: ProgressDialog

    var regex = "[A-Z0-9a-z]+([._%+-][A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var pattern = Pattern.compile(regex)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var filePath = ""
    private lateinit var countryDialog: Dialog
    private var selectedLanguageList: ArrayList<SelectedLanguageModel> = ArrayList()

    private lateinit var countryAdapter: ArrayAdapter<String>
    private var countryList : ArrayList<Country> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .build()
        )

        AppPreferences.initialize(this.applicationContext)
        progressDialog = ProgressDialog(this)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.save.setOnClickListener {
            updateProfile()
            updateLanguage()
        }

//        binding.country.setOnClickListener {
//            allCountry()
//        }

        binding.profileImage.setOnClickListener {
            setupPermissions()
        }

        binding.editPic.setOnClickListener {
            setupPermissions()
        }

        binding.profileLocation.setOnClickListener {
            startActivity(Intent(this,ProfileMapActivity::class.java))
        }

        getUserProfile()
        getUserLanguage()

        countrySpinner()
    }

    private fun updateLanguage() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Id"] = "0"
                map["UserId"] = "0"
                map["LanguageMasterId"] = ""
                map["LanguageName"] = ""
                map["UserName"] = ""

                val response = ServiceApi.retrofitService.userLanguageUpdate(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("update_Language", response.code().toString())
                        Log.d("update_Language", response.body().toString())

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

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Storage Permission Denied")
            makeRequest()
        } else {
            openGallery()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }

    private fun openGallery() {
        Album.image(this)
            .multipleChoice()
            .camera(false)
            .columnCount(4)
            .selectCount(1)
            .onResult {
                filePath = it[0].path
                confirmImageOrder(filePath)
                Glide.with(this)
                    .load(filePath)
                    .into(binding.profileImage)
                Log.d(TAG, "openGallery: ${filePath}")
                Log.d(TAG, "openGallery: ${Gson().toJson(it)}")
            }
            .onCancel {
                Log.d(TAG, "openGallery: $it")
            }
            .start()
    }

    private fun confirmImageOrder(filePath: String) {
        val imageToBeUploaded = File(filePath)
        coroutineScope.launch {
            try {
                /*val rbcustomerid =
                    RequestBody.create(
                        MediaType.parse("text/plain"),
                        AppPreferences.getUserData(Params.UserId)
                    )*/
                val rbcustomerid = AppPreferences.getUserData(Params.UserId).toRequestBody("text/plain".toMediaTypeOrNull())
                val requestFile = MultipartBody.Part.createFormData("Files", "${System.currentTimeMillis()}.png",
                    File(filePath).asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                /*val requestFile =
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        imageToBeUploaded
                    )*/
                /*val imageRequest =
                    MultipartBody.Part.createFormData(
                        "Files",
                        "${System.currentTimeMillis()}.png",
                        requestFile
                    )*/
                val response = ServiceApi.retrofitService.uploadProfilePic(
                    rbcustomerid,
                    requestFile
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "confirmImageOrder: ${response.code()}")
                        Log.d(TAG, "confirmImageOrder:${response.body()}")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "error: ${response.code()}")
                        Log.d(TAG, "error: ${response.body()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "confirmImageOrder: ${e.message}")
                }
            }
        }
    }

    private fun getUserLanguage() {

        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllJobLang(
                    map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getUserLanguages", response.code().toString())
                        Log.d("getUserLanguages", response.body().toString())

                        selectedLanguageList.clear()
                        val list = response.body()!!.values!!
                        getAllLanguages(list)

//                        binding.userLanguageRv.adapter = AllJobLanguagesAdapter(this@Profile, list)
//                        binding.userLanguageRv.layoutManager = LinearLayoutManager(this@Profile, LinearLayoutManager.HORIZONTAL, false)
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

    private fun getAllLanguages(allLanguageList: List<AllJobLanguageList>) {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getUserLanguage(
                    AppPreferences.getUserData(Params.UserId).toInt(),map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getUserLanguages", response.code().toString())
                        Log.d("getUserLanguages", response.body().toString())

                        val list = response.body()!!.values!!

                        allLanguageList.forEachIndexed { index, allJobLanguageList ->
                            list.forEachIndexed { index, userLanguage ->
                                if (userLanguage.languageMasterId == allJobLanguageList.id) {
                                    if (!selectedLanguageList.contains(userLanguage.languageMasterId)) {
                                        selectedLanguageList.add(
                                            SelectedLanguageModel(
                                                userLanguage.languageMasterId,
                                                userLanguage.languageName,
                                                true
                                            )
                                        )
                                    }
                                } else {
                                    if (!selectedLanguageList.contains(userLanguage.languageMasterId)) {
                                        selectedLanguageList.add(
                                            SelectedLanguageModel(
                                                userLanguage.languageMasterId,
                                                userLanguage.languageName,
                                                false
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Log.d(TAG, "selected langauges: ${Gson().toJson(selectedLanguageList)}")

                        binding.userLanguageRv.adapter = AllJobLanguagesAdapter(this@Profile, selectedLanguageList)
                        binding.userLanguageRv.layoutManager = LinearLayoutManager(this@Profile, LinearLayoutManager.HORIZONTAL, false)
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


//    private fun allCountry() {
//        countryDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
//        countryDialog.setContentView(R.layout.country_popup)
//        countryDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)
//        val countryRecycler = countryDialog.findViewById<RecyclerView>(R.id.CountryListRv)
//        countryList(countryRecycler)
//        countryDialog.show()
//    }

    private fun countrySpinner() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getCountry(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getCountryList", response.code().toString())
                        Log.d("getCountryList", response.body().toString())

                        countryList = response.body()?.values as ArrayList<Country>

                        val data : MutableList<String> = ArrayList()
                        countryList.forEach {
                            data.add(it.code.toString())
                        }

                        countryAdapter = object : ArrayAdapter<String>(this@Profile, android.R.layout.simple_list_item_1, data) {}
                        binding.countrySpinner.adapter = countryAdapter

                        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                countryId = countryList[position].id!!.toInt()
                                toast("Selected : " + countryList[position].country)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

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

    private fun updateProfile() {

        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val companyName = binding.companyName.text.toString().trim()
        val address = binding.address.text.toString().trim()

        if (firstName.isEmpty()){
            binding.firstName.error = "Field Can't be Empty"
            binding.firstName.requestFocus()
        } else if (lastName.isEmpty()) {
            binding.lastName.error = "Field Can't be Empty"
            binding.lastName.requestFocus()
        }else if (email.isEmpty()) {
            binding.email.error = "Field Can't be Empty"
            binding.email.requestFocus()
        }else if (!pattern.matcher(email).matches()){
            binding.email.error = "Invalid Email"
            binding.email.requestFocus()
        }else if (companyName.isEmpty()) {
            binding.companyName.error = "Field Can't be Empty"
            binding.companyName.requestFocus()
        }else if(address.isEmpty()){
            binding.address.error = "Field Can't be Empty"
            binding.address.requestFocus()
        }else {
            coroutineScope.launch {
                try {
                    val map = HashMap<String, String>()
                    map["Id"] = AppPreferences.getUserData(Params.UserId)
                    map["UserTypeMasterId"] = AppPreferences.getUserData(Params.UserTypeMasterId)
                    map["FirstName"] = firstName
                    map["LastName"] = lastName
                    map["CompanyName"] = companyName
                    map["Email"] = email
                    map["CountryId"] = countryId.toString()
                    map["Address"] = address
                    map["PhoneNumber"] = AppPreferences.getUserData(Params.MobileNumber)
                    Log.d(TAG, "updateProfile: ${Gson().toJson(map)}")
                    val response = ServiceApi.retrofitService.updateUserProfile(map)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d("updateProfile", response.code().toString())
                            Log.d("updateProfile", response.body().toString())
                            toast(response.body()!!.message!!)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "something wrong")
                            Log.d("updateProfile", response.code().toString())
                            Log.d("updateProfile", response.body().toString())
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }
        }
    }

    private fun getUserProfile() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getUserProfile(
                    AppPreferences.getUserData(Params.UserId).toInt()
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getUserProfile", response.code().toString())
                        Log.d("getUserProfile", response.body().toString())

                        val response = response.body()!!.item!!

                        binding.profileImage.load("http://realestateapi.lamproskids.com/" + response.profileUrl)
                        binding.firstName.setText(response.firstName).toString().trim()
                        binding.lastName.setText(response.lastName).toString().trim()
                        binding.email.setText(response.email).toString().trim()
                        binding.companyName.setText(response.companyName).toString().trim()
                        binding.address.setText(response.address.toString().trim())
//                        binding.countrySpinner = response.countryCode.toString().trim()
                        binding.number.setText(response.phoneNumber).toString().trim()
                        countryId = response.countryId!!

                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                progressDialog.dialog.dismiss()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission has been denied by user")
                } else {
                    openGallery()
                    Log.d(TAG, "Permission has been granted by user")
                }
            }
        }
    }

//    override fun onItemClick(itemPosition: Int, data: Country) {
//        countryDialog.dismiss()
//        binding.country.setText(data.country)
//    }
}