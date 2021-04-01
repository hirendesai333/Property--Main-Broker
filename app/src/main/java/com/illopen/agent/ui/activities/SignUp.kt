package com.illopen.agent.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.databinding.ActivitySignUpBinding
import com.illopen.agent.model.Country
import com.illopen.agent.model.DocumentTypeList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.*
import java.util.regex.Pattern

class SignUp : AppCompatActivity(){

    private val TAG = "SignUpActivity"

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog

    var regex = "[A-Z0-9a-z]+([._%+-][A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var pattern : Pattern = Pattern.compile(regex)

    private val userTypeMasterId : Int = 2

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var countryList: ArrayList<Country> = ArrayList()
    private lateinit var userDropDownAdapter: ArrayAdapter<String>
    private var countrySelectedID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        binding.registerButton.setOnClickListener {
            signUpDetails()
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        countrySelectSpinner()
    }

    private fun countrySelectSpinner() {
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

                        Log.d("countrySelectSpinner", response.code().toString())
                        Log.d("countrySelectSpinner", response.body().toString())

                        countryList = response.body()?.values as ArrayList<Country>

                        val data: MutableList<String> = ArrayList()

                        countryList.forEach {
                            data.add(it.country.toString())
                        }

                        userDropDownAdapter = object : ArrayAdapter<String>(
                            this@SignUp,
                            android.R.layout.simple_list_item_1, data
                        ) {}
                        binding.countrySpinner.adapter = userDropDownAdapter

                        binding.countrySpinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?,
                                    position: Int, id: Long
                                ) {
                                    countrySelectedID = countryList[position].id!!.toInt()
                                    toast("Selected : " + countryList[position].country)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    //nothing select code
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

    private fun signUpDetails() {
        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()
        val companyName = binding.companyName.text.toString().trim()
        val email = binding.emailID.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val phoneNumber = binding.phone.text.toString().trim()
        if (firstName.isEmpty()){
            binding.firstName.error = "Please Enter First Name"
            binding.firstName.requestFocus()
        } else if (lastName.isEmpty()) {
            binding.lastName.error = "Please Enter Last Name"
            binding.lastName.requestFocus()
        } else if (companyName.isEmpty()) {
            binding.companyName.error = "Please Enter Company Name"
            binding.companyName.requestFocus()
        } else if (email.isEmpty()) {
            binding.emailID.error = "Please Enter Email"
            binding.emailID.requestFocus()
        }else if (!pattern.matcher(email).matches()){
            binding.emailID.error = "Invalid Email"
            binding.emailID.requestFocus()
        }else if (password.isEmpty ()){
            binding.password.error = "Please Enter Password"
            binding.password.requestFocus()
        }else if (password.length < 8){
            binding.password.error = "Password Should be 8 or more Character"
            binding.password.requestFocus()
        }else if(phoneNumber.isEmpty()){
            binding.phone.error = "Please Enter Number"
            binding.phone.requestFocus()
        }else if(phoneNumber.length < 10){
            binding.phone.error = "Invalid Number"
            binding.phone.requestFocus()
        }
        else {
            progressDialog.dialog.show()
            coroutineScope.launch {
                try {
                    val map = HashMap<String,String>()
                    map["UserTypeMasterId"] = userTypeMasterId.toString()
                    map["FirstName"] = firstName
                    map["LastName"] = lastName
                    map["CompanyName"] = companyName
                    map["Email"] = email
                    map["Password"] = password
                    map["CountryId"] = countrySelectedID.toString()
                    map["PhoneNumber"] = phoneNumber

                    val response = ServiceApi.retrofitService.signUp(map)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            if (response.code() == 200) {
                                Log.d("SignUp", response.code().toString())
                                Log.d("SignUp", response.body().toString())
                                toast("SignUp Success")
                                startActivity(Intent(this@SignUp, LoginActivity::class.java))
                            } else {
                                toast("Please try again")
                            }
                            progressDialog.dialog.dismiss()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.d("SignUp fail", response.code().toString())
                            toast("please enter all details")
                            progressDialog.dialog.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                    progressDialog.dialog.dismiss()
                }
            }
        }
    }
}