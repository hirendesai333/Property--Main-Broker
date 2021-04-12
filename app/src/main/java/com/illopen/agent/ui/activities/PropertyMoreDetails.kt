package com.illopen.agent.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.illopen.agent.R
import com.illopen.agent.adapters.PropertyMoreDetailsAdapter
import com.illopen.agent.databinding.ActivityPropertyMoreDetailsBinding
import com.illopen.agent.model.PropertyMoreDetailsList
import com.illopen.agent.model.PropertyMoreDetailsTypeList
import com.illopen.agent.network.ServiceApi
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*

class PropertyMoreDetails : AppCompatActivity() , PropertyMoreDetailsAdapter.OnItemClickListener, PropertyMoreDetailsAdapter.OnItemEditClickListener {

    private lateinit var binding: ActivityPropertyMoreDetailsBinding

    private val TAG = "PropertyMoreDetails"

    lateinit var propertyName: String

    private var propertyTypeMasterId: Int = 0

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var propertyTypeList: ArrayList<PropertyMoreDetailsTypeList> = ArrayList()
    private lateinit var userDropDownAdapter: ArrayAdapter<String>
    private var PropertyDetailMasterId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.setOnClickListener {
            onBackPressed()
        }

        binding.addNew.setOnClickListener {
            addNewProperty()
        }

        propertyTypeMasterId = intent.getIntExtra("PropertyMasterId",0)

        propertyName = intent.getStringExtra("propertyName")!!
        binding.title.text = propertyName

        propertyMoreDetailsAPI()
    }

    private fun spinnerPropertyType(mDialog: Dialog) {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.propertyDetailsType(map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("PropertyDetailsType", response.code().toString())
                        Log.d("PropertyDetailsType", response.body().toString())

                        propertyTypeList = response.body()?.values as ArrayList<PropertyMoreDetailsTypeList>

                        val data: MutableList<String> = ArrayList()

                        propertyTypeList.forEach {
                            data.add(it.name.toString())
                        }

                        userDropDownAdapter = object : ArrayAdapter<String>(
                            this@PropertyMoreDetails,
                            android.R.layout.simple_list_item_1, data
                        ) {}
                        mDialog.findViewById<Spinner>(R.id.TypeSpinner).adapter = userDropDownAdapter

                        mDialog.findViewById<Spinner>(R.id.TypeSpinner).onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?,
                                    position: Int, id: Long
                                ) {
                                    PropertyDetailMasterId  = propertyTypeList[position].id!!.toInt()
                                    toast("Selected : " + propertyTypeList[position].name)
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

    @SuppressLint("CutPasteId")
    private fun addNewProperty() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.add_new_more_property_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

            spinnerPropertyType(mDialog)

        mDialog.findViewById<Button>(R.id.save).setOnClickListener {
            val price = mDialog.findViewById<TextInputEditText>(R.id.price).text.toString().trim()

            if (price.isEmpty()) {
                mDialog.findViewById<TextInputEditText>(R.id.price).error = "Field Can't be Empty"
                mDialog.findViewById<TextInputEditText>(R.id.price).requestFocus()
            } else {
                Toast.makeText(this, "Added New Property Details..", Toast.LENGTH_SHORT).show()
                addNewPropertyAPI(price)
                mDialog.dismiss()
            }
        }
        mDialog.show()
    }

    private fun addNewPropertyAPI(price: String) {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
//                map["Id"] = "0"
                map["PropertyMasterId"] = propertyTypeMasterId.toString()
                map["PropertyDetailMasterId"] = PropertyDetailMasterId.toString()
                map["Value"] = price

                val response = ServiceApi.retrofitService.propertyMoreDetailsInsert(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Property_Inserted", response.code().toString())
                        Log.d("Property_Inserted", response.body().toString())

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

    private fun propertyMoreDetailsAPI() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.propertyMoreDetailsAll(propertyTypeMasterId,map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("PropertyMoreDetails", response.code().toString())
                        Log.d("PropertyMoreDetails", response.body().toString())

                        val list: List<PropertyMoreDetailsList> = response.body()!!.values!!

                        binding.moreDetails.adapter =
                            PropertyMoreDetailsAdapter(this@PropertyMoreDetails, list,
                                this@PropertyMoreDetails,this@PropertyMoreDetails)
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

    override fun onDeleteProperty(itemPosition: Int, data: PropertyMoreDetailsList) {
        //todo here delete id and delete by same id's so issue in api side//
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("DELETE")
        dialog.setMessage("Are You Sure You Want to Delete?")

        dialog.setPositiveButton("YES") { dialog: DialogInterface, i: Int ->
            deleteProperty(data)
        }

        dialog.setNegativeButton("NO") { dialog: DialogInterface, i: Int ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteProperty(data: PropertyMoreDetailsList) {
        coroutineScope.launch {
            try {
                val response = ServiceApi.retrofitService.deletePropertyMoreDetails(
                    data.id!!,
                    data.id
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("delete_Property_More", response.code().toString())
                        Log.d("delete_Property_More", response.body().toString())

                        toast("Delete Property More Details Successfully")
                        finish()
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

    override fun onItemEditClick(itemPosition: Int, data: PropertyMoreDetailsList) {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.update_property_more_details_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val spinner = mDialog.findViewById<Spinner>(R.id.TypeSpinner)
        val price = mDialog.findViewById<TextInputEditText>(R.id.price)

        spinnerPropertyType(mDialog)

        spinner?.selectedItem.toString()
        price?.setText(data.value)

        mDialog.show()

        mDialog.findViewById<MaterialButton>(R.id.save).setOnClickListener {

            val typeSpinner = spinner.selectedItem.toString()
            val value = price.text.toString().trim()

            Toast.makeText(this, "Update Property..", Toast.LENGTH_SHORT).show()
            updateProperty(typeSpinner,value,data)
            mDialog.dismiss()
        }
    }

    private fun updateProperty(typeSpinner: String, value: String, data: PropertyMoreDetailsList) {
        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Id"] = data.id.toString()
                map["PropertyMasterId"] = propertyTypeMasterId.toString()
                map["PropertyDetailMasterId"] = PropertyDetailMasterId.toString()
                map["Value"] = value

                val response = ServiceApi.retrofitService.updatePropertyMoreDetails(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("updateProperty", response.code().toString())
                        Log.d("updateProperty", response.body().toString())

                        startActivity(Intent(this@PropertyMoreDetails,MyProperties::class.java))
                        finish()
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