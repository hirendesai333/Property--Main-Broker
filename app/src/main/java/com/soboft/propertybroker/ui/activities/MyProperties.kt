 package com.soboft.propertybroker.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.soboft.propertybroker.adapters.MyCustomersAdapter
import com.soboft.propertybroker.adapters.MyPropertiesAdapter
import com.soboft.propertybroker.databinding.ActivityMyPropertiesBinding
import com.soboft.propertybroker.model.Value
import com.soboft.propertybroker.model.Values
import com.soboft.propertybroker.network.ServiceApi
import com.soboft.propertybroker.utils.AppPreferences
import com.soboft.propertybroker.utils.Params
import kotlinx.coroutines.*

 class MyProperties : AppCompatActivity() {

    lateinit var binding: ActivityMyPropertiesBinding

    private val TAG = "MyProperties"

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPropertiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)

        binding.title.setOnClickListener {
            onBackPressed()
        }

//        binding.myPropertiesRv.adapter = MyPropertiesAdapter(this)

        binding.addNew.setOnClickListener {
            startActivity(Intent(this, AddProperty::class.java))
        }

        getAllProperties()
    }

    private fun getAllProperties() {
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.getAllProperties(
                    AppPreferences.getUserData(Params.UserId).toInt()
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getAllProperty", response.code().toString())
                        Log.d("getAllProperty",response.body().toString())

                        val list : List<Values> = response.body()!!.values!!

                        binding.myPropertiesRv.adapter = MyPropertiesAdapter(this@MyProperties,list)
                        binding.myPropertiesRv.layoutManager = LinearLayoutManager(this@MyProperties)

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
}