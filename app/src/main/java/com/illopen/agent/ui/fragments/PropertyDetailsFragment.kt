package com.illopen.agent.ui.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.illopen.agent.R
import com.illopen.agent.adapters.PropertyMoreDetailsAdapter
import com.illopen.agent.databinding.FragmentPropertyDetailsBinding
import com.illopen.agent.model.PropertyMoreDetailsList
import com.illopen.agent.model.PropertyMoreDetailsTypeList
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*

class PropertyDetailsFragment : Fragment(R.layout.fragment_property_details) ,
    PropertyMoreDetailsAdapter.OnItemClickListener,
    PropertyMoreDetailsAdapter.OnItemEditClickListener {

    private val TAG: String = "PropertyDetailsFragment"
    private var _binding: FragmentPropertyDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressDialog : ProgressDialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    lateinit var propertyName: String

    private var propertyTypeMasterId : Int = 0

    lateinit var mDialog : Dialog

    private var propertyTypeList: ArrayList<PropertyMoreDetailsTypeList> = ArrayList()
    private lateinit var userDropDownAdapter: ArrayAdapter<String>

    private var propertyDetailMasterId : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPropertyDetailsBinding.bind(view)

        progressDialog = ProgressDialog(requireContext())

        propertyTypeMasterId = activity?.intent!!.getIntExtra("PropertyMasterId",0)

        propertyMoreDetailsAPI()

        binding.addNew.setOnClickListener {
            addProperty()
        }
    }


    private fun addProperty() {
        mDialog = Dialog(requireContext(), R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.add_new_more_property_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        spinnerPropertyType(mDialog)

        mDialog.findViewById<Button>(R.id.save).setOnClickListener {
            val price = mDialog.findViewById<TextInputEditText>(R.id.price).text.toString().trim()

            if (price.isEmpty()) {
                mDialog.findViewById<TextInputEditText>(R.id.price).error = "Field Can't be Empty"
                mDialog.findViewById<TextInputEditText>(R.id.price).requestFocus()
            } else {
                Toast.makeText(requireContext(), "Added New Property Details..", Toast.LENGTH_SHORT).show()
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
                map["PropertyDetailMasterId"] = propertyDetailMasterId.toString()
                map["Value"] = price

                val response = ServiceApi.retrofitService.propertyMoreDetailsInsert(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Property_Inserted", response.code().toString())
                        Log.d("Property_Inserted", response.body().toString())

                        if (response.code() == 200){
                            propertyMoreDetailsAPI()
                            requireActivity().toast("Property Inserted Successfully")
                        }else{
                            requireActivity().toast("something wrong")
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
                        binding.moreDetails.adapter = PropertyMoreDetailsAdapter(requireContext(), list,
                            this@PropertyDetailsFragment,this@PropertyDetailsFragment)
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
        val dialog = MaterialAlertDialogBuilder(requireContext())
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

                        Log.d("delete_Property", response.code().toString())
                        Log.d("delete_Property", response.body().toString())

                        if (response.code() == 200){
                            propertyMoreDetailsAPI()
                            requireActivity().toast("Delete Property Details Successfully")
                        }else{
                            requireActivity().toast("something wrong")
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

    override fun onItemEditClick(itemPosition: Int, data: PropertyMoreDetailsList) {
        val mDialog = Dialog(requireContext(), R.style.Theme_PropertyMainBroker)
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

            Toast.makeText(requireContext(), "Update Property..", Toast.LENGTH_SHORT).show()
            updateProperty(typeSpinner,value,data)
            mDialog.dismiss()
        }
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
                            requireContext(),
                            android.R.layout.simple_list_item_1, data
                        ) {}
                        mDialog.findViewById<Spinner>(R.id.TypeSpinner).adapter = userDropDownAdapter

                        mDialog.findViewById<Spinner>(R.id.TypeSpinner).onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?,
                                    position: Int, id: Long
                                ) {
                                    propertyDetailMasterId  = propertyTypeList[position].id!!.toInt()
                                    Toast.makeText(requireContext(),"Selected : ${propertyTypeList[position].name}",Toast.LENGTH_LONG).show()
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

    private fun updateProperty(typeSpinner: String, value: String, data: PropertyMoreDetailsList) {
        coroutineScope.launch {
            try {

                val map = HashMap<String,String>()
                map["Id"] = data.id.toString()
                map["PropertyMasterId"] = propertyTypeMasterId.toString()
                map["PropertyDetailMasterId"] = propertyDetailMasterId.toString()
                map["Value"] = value

                val response = ServiceApi.retrofitService.updatePropertyMoreDetails(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("updateProperty", response.code().toString())
                        Log.d("updateProperty", response.body().toString())

                        if (response.code() == 200){
                            requireActivity().toast("update Successfully")
                            propertyMoreDetailsAPI()
                        }else{
                            requireActivity().toast("something wrong")
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
}