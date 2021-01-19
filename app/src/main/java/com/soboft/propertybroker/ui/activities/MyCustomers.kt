package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.customerRv.adapter = MyCustomersAdapter()

        binding.addNew.setOnClickListener {
            addNewCustomer()
        }

        getCustomer()
    }

    private fun getCustomer() {

        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getAllCustomer(
                    0
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
    }
}