 package com.illopen.agent.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.MyPropertiesAdapter
import com.illopen.agent.databinding.ActivityMyPropertiesBinding
import com.illopen.agent.model.Values
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*

 class MyProperties : AppCompatActivity(),MyPropertiesAdapter.OnItemClickListner {

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

        binding.addNew.setOnClickListener {
            Intent(this, AddProperty::class.java).apply {
                putExtra("poropertyId", "0")
                putExtra("from", "new")
                startActivity(this)
            }
        }

        getAllProperties()
    }

    private fun getAllProperties() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllProperties(
                    AppPreferences.getUserData(Params.UserId).toInt(),map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getAllProperty", response.code().toString())
                        Log.d("getAllProperty",response.body().toString())

                        val list : List<Values> = response.body()!!.values!!

                        binding.myPropertiesRv.adapter = MyPropertiesAdapter(this@MyProperties,list,this@MyProperties)

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

     private fun deleteProperty(data: Values) {
         coroutineScope.launch {
             try {
                 val response = ServiceApi.retrofitService.deleteProperty(
                     data.id!!
                 )

                 if (response.isSuccessful){
                     withContext(Dispatchers.Main){

                         Log.d("deleteProperty", response.code().toString())
                         Log.d("deleteProperty", response.body().toString())

                         toast("Delete Property Successfully")
                         startActivity(Intent(this@MyProperties,MyProperties::class.java))
                         finish()
                     }
                 }else{
                     withContext(Dispatchers.Main){
                         Log.d(TAG, "Property not deleted ")
                     }
                 }
             }catch (e : Exception){
                 Log.d(TAG, e.message.toString())
             }
         }
     }

     override fun onItemClick(position: Int, data: Values) {
         val builder = AlertDialog.Builder(this)
         builder.setTitle("Are You Sure Delete!")
         builder.setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
             deleteProperty(data)
         }
         builder.setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
             dialogInterface.dismiss()
         }
         builder.show()
     }
 }