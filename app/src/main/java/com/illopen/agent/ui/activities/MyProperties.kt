package com.illopen.agent.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.MyPropertiesAdapter
import com.illopen.agent.databinding.ActivityMyPropertiesBinding
import com.illopen.agent.model.AllPropertiesList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashMap

class MyProperties : AppCompatActivity(), MyPropertiesAdapter.OnItemClickListner {

    lateinit var binding: ActivityMyPropertiesBinding

    private val TAG = "MyProperties"

    private lateinit var progressDialog: ProgressDialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPropertiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)
        progressDialog = ProgressDialog(this)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.addNew.setOnClickListener {
            Intent(this, AddProperty::class.java).apply {
                putExtra("propertyId", "0")
                putExtra("from", "new")
                startActivity(this)
            }
        }

        getAllProperties()
    }

    private fun getAllProperties() {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllProperties(
                    AppPreferences.getUserData(Params.UserId).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getAllProperty", response.code().toString())
                        Log.d("getAllProperty", response.body().toString())

                        if (response.code() == 200) {
                            val list: List<AllPropertiesList> = response.body()!!.values!!

                            Collections.reverse(list)

                            binding.myPropertiesRv.adapter =
                                MyPropertiesAdapter(this@MyProperties, list, this@MyProperties)


                        } else {

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

    private fun deleteProperty(data: AllPropertiesList) {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.deleteProperty(
                    data.id!!, data.id!!
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("deleteProperty", response.code().toString())
                        Log.d("deleteProperty", response.body().toString())

                        if (response.code() == 200) {
                            getAllProperties()
                            toast("Delete Property Successfully")
                            startActivity(Intent(this@MyProperties, MyProperties::class.java))
                            finish()
                        } else {
                            toast("property not deleted")
                        }
                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "Property not deleted ")
                        progressDialog.dialog.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                progressDialog.dialog.dismiss()
            }
        }
    }

    override fun onItemClick(position: Int, data: AllPropertiesList) {
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