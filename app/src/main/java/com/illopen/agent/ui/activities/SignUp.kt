package com.illopen.agent.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import com.illopen.agent.R
import com.illopen.agent.databinding.ActivitySignUpBinding
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.regex.Pattern

class SignUp : AppCompatActivity() , CountryCodePicker.OnCountryChangeListener{

    private val TAG = "SignUpActivity"

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog

    private var countryPicker : CountryCodePicker? = null
    private var countryCode : String? = null
    private var countryName : String? = null

    var regex = "[A-Z0-9a-z]+([._%+-][A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var pattern : Pattern = Pattern.compile(regex)

//    private var countryID : Int = 0
    private val userTypeMasterId : Int = 2

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

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

        countryPicker = findViewById(R.id.country_code_picker)
        countryPicker!!.setOnCountryChangeListener(this)
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
        }else if (password.length <= 8){
            binding.password.error = "Password Should be 8 or more Character"
            binding.password.requestFocus()
        }else if(phoneNumber.isEmpty()){
            binding.phone.error = "Please Enter Number"
            binding.phone.requestFocus()
        }else if(phoneNumber.length <= 10){
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
                    map["CountryId"] = "1"
                    map["PhoneNumber"] = phoneNumber

                    val response = ServiceApi.retrofitService.signup(map)
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

    override fun onCountrySelected() {
        countryCode = countryPicker!!.selectedCountryCode
        countryName = countryPicker!!.selectedCountryName

//        Toast.makeText(this, "Country Code $countryCode",Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Country Name $countryName",Toast.LENGTH_SHORT).show()
    }
}