package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.MyCustomersAdapter
import com.soboft.propertybroker.databinding.ActivityMyCustomersBinding
import com.soboft.propertybroker.model.AllCustomerModel
import com.soboft.propertybroker.model.Value
import com.soboft.propertybroker.network.ServiceApi
import kotlinx.coroutines.*

class MyCustomers : AppCompatActivity() {

    lateinit var binding: ActivityMyCustomersBinding

    private val TAG = "MyCustomerActivity"

    var id : Int = 0

    var userId : Int = 2

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNew.setOnClickListener {
            addNewCustomer()
        }

        getCustomer()
    }

    private fun getCustomer() {

        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getAllCustomer(
                    id
                )
            if (response.isSuccessful){
                withContext(Dispatchers.Main){

                    Log.d("getCustomer", response.code().toString())
                    Log.d("getCustomer",response.body().toString())

                    val list : List<Value> = response.body()!!.values!!

                    binding.customerRv.adapter = MyCustomersAdapter(this@MyCustomers,list)
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

            Toast.makeText(this, "save Data..", Toast.LENGTH_SHORT).show()
            addNewCustomerApi()
            mDialog.dismiss()
        }

    }

    private fun addNewCustomerApi() {

        coroutineScope.launch {
            try {
                val data = HashMap<String,String>()
                data["id"] = id.toString()
                data["UserId"] = userId.toString()
                data["CustomerName"] = ""
                data["CustomerEmail"] = ""
                data["CustomerCountryCode"] = ""
                data["CustomerPhoneNumber"] = ""
                val response = ServiceApi.retrofitService.addCustomer(data)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("addNewCustomer", response.code().toString())
                        Log.d("addNewCustomer", response.body().toString())
                        Toast.makeText(this@MyCustomers, "Save APi success", Toast.LENGTH_SHORT).show()
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