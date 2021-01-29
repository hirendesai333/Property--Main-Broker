package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.MyCustomersAdapter
import com.soboft.propertybroker.databinding.ActivityMyCustomersBinding
import com.soboft.propertybroker.model.Value
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import kotlinx.coroutines.*

class MyCustomers : AppCompatActivity(), MyCustomersAdapter.OnItemClickListener {

    lateinit var binding: ActivityMyCustomersBinding

    private val TAG = "MyCustomerActivity"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)

        binding.addNew.setOnClickListener {
            addNewCustomer()
        }

        getCustomer()
    }

    private fun getCustomer() {

        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getAllCustomer(
                    AppPreferences.getUserData(Params.UserId).toInt()
                )
            if (response.isSuccessful){
                withContext(Dispatchers.Main){

                    Log.d("getCustomer", response.code().toString())
                    Log.d("getCustomer",response.body().toString())

                    val list : List<Value> = response.body()!!.values!!

                    binding.customerRv.adapter = MyCustomersAdapter(applicationContext,list,this@MyCustomers)
                    binding.customerRv.layoutManager = LinearLayoutManager(this@MyCustomers)

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

    private fun addNewCustomer() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.add_customer_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        mDialog.show()

        mDialog.findViewById<Button>(R.id.add).setOnClickListener {
            val name = mDialog.findViewById<TextInputEditText>(R.id.firstName).text.toString().trim()
            val email = mDialog.findViewById<TextInputEditText>(R.id.emailid).text.toString().trim()
            val phone = mDialog.findViewById<TextInputEditText>(R.id.phone).text.toString().trim()

            Toast.makeText(this, "Added New Customer..", Toast.LENGTH_SHORT).show()
            addNewCustomerApi(name,email,phone)
            mDialog.dismiss()
        }

    }

    private fun addNewCustomerApi(name: String, email: String, phone: String) {

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()
//                data["id"] = userID.toString()
                data["UserId"] = AppPreferences.getUserData(Params.UserId)
                data["CustomerName"] = name
                data["CustomerEmail"] = email
                data["CustomerCountryCode"] = "1"
                data["CustomerPhoneNumber"] = phone
                val response = ServiceApi.retrofitService.addCustomer(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("addNewCustomer", response.code().toString())
                        Log.d("addNewCustomer", response.body().toString())

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

    override fun onItemClick(itemPosition: Int, data: Value) {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
            mDialog.setContentView(R.layout.update_customer_popup)
            mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val tvFirstName = mDialog.findViewById<TextInputEditText>(R.id.updatefirstName)
        val tvEmail = mDialog.findViewById<TextInputEditText>(R.id.updateemailid)
        val tvPhone = mDialog.findViewById<TextInputEditText>(R.id.updatephone)

//        println(itemPosition)
//        println(data.id)

        tvFirstName?.setText(data.customerName)
        tvEmail?.setText(data.customerEmail)
        tvPhone?.setText(data.phoneNumber)

        mDialog.show()

        mDialog.findViewById<Button>(R.id.update).setOnClickListener {
            val name = tvFirstName.text.toString().trim()
            val email = tvEmail.text.toString().trim()
            val phone = tvPhone.text.toString().trim()

            Toast.makeText(this, "Update Customer..", Toast.LENGTH_SHORT).show()
            updateCustomer(name,email,phone,data)
            mDialog.dismiss()
        }

    }

    private fun updateCustomer(name: String,email: String,phone: String,data: Value) {
        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["UserId"] = data.userId.toString()
                map["Id"] =  data.id.toString()
                map["CustomerName"] = name
                map["CustomerEmail"] = email
                map["CustomerCountryCode"] = "1"
                map["CustomerPhoneNumber"] = phone

                val response = ServiceApi.retrofitService.updateCustomer(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("updateCustomer", response.code().toString())
                        Log.d("updateCustomer", response.body().toString())

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