package com.illopen.agent.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.illopen.agent.R
import com.illopen.agent.adapters.MyCustomersAdapter
import com.illopen.agent.databinding.ActivityMyCustomersBinding
import com.illopen.agent.model.Country
import com.illopen.agent.model.Value
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.regex.Pattern

class MyCustomers : AppCompatActivity(), MyCustomersAdapter.OnItemClickListener {

    lateinit var binding: ActivityMyCustomersBinding

    private val TAG = "MyCustomerActivity"

    private lateinit var progressDialog: ProgressDialog

    var regex =
        "[A-Z0-9a-z]+([._%+-][A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var pattern = Pattern.compile(regex)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var countryAdapter: ArrayAdapter<String>
    private var countryList: ArrayList<Country> = ArrayList()
    var countryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)
        progressDialog = ProgressDialog(this)

        binding.addNew.setOnClickListener {
            addNewCustomer()
        }

        binding.title.setOnClickListener { onBackPressed() }

        getCustomer()
    }

    private fun countrySpinner(mDialog: Dialog) {
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

                        val data: MutableList<String> = ArrayList()
                        countryList.forEach {
                            data.add(it.code.toString())
                        }

                        countryAdapter = object : ArrayAdapter<String>(
                            this@MyCustomers,
                            android.R.layout.simple_list_item_1,
                            data
                        ) {}
                        mDialog.findViewById<Spinner>(R.id.countrySpinner).adapter = countryAdapter

                        mDialog.findViewById<Spinner>(R.id.countrySpinner).onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    countryId = countryList[position].id!!.toInt()
//                                toast("Selected : " + countryList[position].code)
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

    private fun getCustomer() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllCustomer(
                    AppPreferences.getUserData(Params.UserId).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getCustomer", response.code().toString())
                        Log.d("getCustomer", response.body().toString())

                        val list: List<Value> = response.body()!!.values!!

                        binding.customerRv.adapter =
                            MyCustomersAdapter(applicationContext, list, this@MyCustomers)
                        binding.customerRv.layoutManager = LinearLayoutManager(this@MyCustomers)

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

    @SuppressLint("CutPasteId")
    private fun addNewCustomer() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.add_customer_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        countrySpinner(mDialog)

        mDialog.show()

        mDialog.findViewById<Button>(R.id.add).setOnClickListener {
            val name =
                mDialog.findViewById<TextInputEditText>(R.id.firstName).text.toString().trim()
            val email = mDialog.findViewById<TextInputEditText>(R.id.emailid).text.toString().trim()
            val phone = mDialog.findViewById<TextInputEditText>(R.id.phone).text.toString().trim()

            if (name.isEmpty()) {
                mDialog.findViewById<TextInputEditText>(R.id.firstName).error =
                    "Field Can't be Empty"
                mDialog.findViewById<TextInputEditText>(R.id.firstName).requestFocus()
            } else if (email.isEmpty()) {
                mDialog.findViewById<TextInputEditText>(R.id.emailid).error = "Field Can't be Empty"
                mDialog.findViewById<TextInputEditText>(R.id.emailid).requestFocus()
            } else if (!pattern.matcher(email).matches()) {
                mDialog.findViewById<TextInputEditText>(R.id.emailid).error = "Invalid Email"
                mDialog.findViewById<TextInputEditText>(R.id.emailid).requestFocus()
            } else if (phone.isEmpty()) {
                mDialog.findViewById<TextInputEditText>(R.id.phone).error = "Field Can't be Empty"
                mDialog.findViewById<TextInputEditText>(R.id.phone).requestFocus()
            } else if (phone.length < 10) {
                mDialog.findViewById<TextInputEditText>(R.id.phone).error = "Invalid Number"
                mDialog.findViewById<TextInputEditText>(R.id.phone).requestFocus()
            } else {
                addNewCustomerApi(name, email, phone)
                mDialog.dismiss()
            }
        }

    }

    private fun addNewCustomerApi(name: String, email: String, phone: String) {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val data = HashMap<String, String>()
                data["UserId"] = AppPreferences.getUserData(Params.UserId)
                data["CustomerName"] = name
                data["CustomerEmail"] = email
                data["CustomerCountryCode"] = countryId.toString()
                data["CustomerPhoneNumber"] = phone
                val response = ServiceApi.retrofitService.addCustomer(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("addNewCustomer", response.code().toString())
                        Log.d("addNewCustomer", response.body().toString())

                        if (response.code() == 200){
                            toast( "Added New Customer Successfully")
                            startActivity(Intent(this@MyCustomers, MyCustomers::class.java))
                            finish()
                        }else{

                        }
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

    override fun onItemClick(itemPosition: Int, data: Value) {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.update_customer_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val tvFirstName = mDialog.findViewById<TextInputEditText>(R.id.updatefirstName)
        val tvEmail = mDialog.findViewById<TextInputEditText>(R.id.updateemailid)
        val tvPhone = mDialog.findViewById<TextInputEditText>(R.id.updatephone)


        tvFirstName?.setText(data.customerName)
        tvEmail?.setText(data.customerEmail)
        tvPhone?.setText(data.phoneNumber)

        countrySpinner(mDialog)

        mDialog.show()

        mDialog.findViewById<Button>(R.id.update).setOnClickListener {
            val name = tvFirstName.text.toString().trim()
            val email = tvEmail.text.toString().trim()
            val phone = tvPhone.text.toString().trim()

            updateCustomer(name, email, phone, data)
            mDialog.dismiss()
        }

    }

    private fun updateCustomer(name: String, email: String, phone: String, data: Value) {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["UserId"] = data.userId.toString()
                map["Id"] = data.id.toString()
                map["CustomerName"] = name
                map["CustomerEmail"] = email
                map["CustomerCountryCode"] = countryId.toString()
                map["CustomerPhoneNumber"] = phone

                val response = ServiceApi.retrofitService.updateCustomer(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("updateCustomer", response.code().toString())
                        Log.d("updateCustomer", response.body().toString())

                        if (response.code() == 200){
                            startActivity(Intent(this@MyCustomers, MyCustomers::class.java))
                            finish()
                        }else{

                        }
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
}