package com.illopen.agent.ui.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.illopen.agent.R
import com.illopen.agent.adapters.ChoosePropertyAdapter
import com.illopen.agent.adapters.JobLanguagesAdapter
import com.illopen.agent.databinding.ActivityNewJob2Binding
import com.illopen.agent.model.AllJobLanguageList
import com.illopen.agent.model.AllPropertiesList
import com.illopen.agent.model.Value
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.android.synthetic.main.activity_new_job2.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class NewJob : AppCompatActivity(), ChoosePropertyAdapter.OnItemClickListener,
    JobLanguagesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityNewJob2Binding

    private val TAG = "CreateNewJob"

    private var selectedList: ArrayList<String> = ArrayList()
    private var selectedLanguageList: ArrayList<String> = ArrayList()

    private lateinit var progressDialog: ProgressDialog

    private var propertyId: Int = 0
    private var customerId: Int = 0

    private lateinit var propertyDialog: Dialog

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var customerListValue: ArrayList<Value> = ArrayList()
    private lateinit var customerListAdapterSpinner: ArrayAdapter<String>
    private var propertyArray = JSONArray()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJob2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)
        progressDialog = ProgressDialog(this)

        binding.backBtn.setOnClickListener { onBackPressed() }

        binding.addNewProperty.setOnClickListener {
            Intent(this, MyProperties::class.java).apply {
                startActivity(this)
            }
        }

        binding.addNewCustomer.setOnClickListener {
            Intent(this, MyCustomers::class.java).apply {
                startActivity(this)
            }
        }

        binding.createNewJob.setOnClickListener {
            createNewJob()
        }

        binding.date.setOnClickListener {

            val now = Date()
            val calendar = Calendar.getInstance()
            calendar.time = Date(now.time)
            val maxDate = calendar.time
            SingleDateAndTimePickerDialog.Builder(this)
                .defaultDate(maxDate)
                .mustBeOnFuture()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayMonth(true)
                .displayYears(true)
                .displayDaysOfMonth(true)
                .displayMonthNumbers(true)
                .displayDays(false)
                .title("Select Date")
                .listener {
                    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    selectedDate = dateFormat.format(it).toString()
                    binding.date.text = selectedDate
                }
                .display()

        }

        binding.time.setOnClickListener {

            SingleDateAndTimePickerDialog.Builder(this)
                .curved()
                .displayMinutes(true)
                .displayHours(true)
                .displayMinutes(true)
                .displayMonth(false)
                .displayYears(false)
                .displayDaysOfMonth(false)
                .displayMonthNumbers(false)
                .displayDays(false)
                .title("Select time")
                .listener {
                    val dateFormat: DateFormat = SimpleDateFormat("hh:mm aa", Locale.US)
                    selectedTime = dateFormat.format(it).toString()
                    binding.time.text = selectedTime
                }
                .display()
        }

        binding.property.setOnClickListener {
            chooseProperty()
        }

        chooseCustomer()
        allLanguages()
    }

    private fun allLanguages() {
        coroutineScope.launch {
            try {

                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllJobLang(map)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("getAllLanguages", response.code().toString())
                        Log.d("getAllLanguages", response.body().toString())

                        val list = response.body()!!.values!!

                        binding.jobLanguage.adapter =
                            JobLanguagesAdapter(this@NewJob, list, this@NewJob)
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

    private fun chooseCustomer() {
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

                        customerListValue = response.body()?.values as ArrayList<Value>

                        val data: MutableList<String> = ArrayList()
//                        data.add(-1,"Select Customer")
                        customerListValue.forEach {
                            data.add(it.customerName.toString())
                        }

                        customerListAdapterSpinner = object : ArrayAdapter<String>(
                            this@NewJob,
                            android.R.layout.simple_list_item_1,
                            data
                        ) {}
                        binding.customerSpinner.adapter = customerListAdapterSpinner
                        binding.customerSpinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    customerId = customerListValue[position].id!!
//                                    toast("Selected : " + customerListValue[position].customerName)
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

    private fun chooseProperty() {

        propertyDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        propertyDialog.setContentView(R.layout.choose_property_popup)
        propertyDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        val propertyRecycler = propertyDialog.findViewById<RecyclerView>(R.id.PropertyRv)
        val button = propertyDialog.findViewById<Button>(R.id.addPropertyBtn)

        button.setOnClickListener { propertyDialog.dismiss() }
        propertyList(propertyRecycler)
        propertyDialog.show()
    }

    private fun propertyList(propertyRecycler: RecyclerView?) {

        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.getAllProperties(
                    AppPreferences.getUserData(
                        Params.UserId
                    ).toInt(), map
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("PropertyType", response.code().toString())
                        Log.d("PropertyType", response.body().toString())

                        val list: List<AllPropertiesList> = response.body()!!.values!!

                        Collections.reverse(list)

                        propertyRecycler!!.adapter =
                            ChoosePropertyAdapter(this@NewJob, list, this@NewJob)
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

    private fun createNewJob() {
        val remark = binding.remarks.text.toString().trim()

        if (customerId == 0) {
            toast("please select customer")
        } else if (propertyId == 0) {
            toast("please select property")
        } else if (selectedDate.isEmpty()) {
            toast("please select date")
        } else if (selectedTime.isEmpty()) {
            toast("please select Time")
        } else if (remark.isEmpty()) {
            toast("please enter Remark")
        } else if (selectedLanguageList.size == 0) {
            toast("please select language")
        } else {
            progressDialog.dialog.show()
            coroutineScope.launch {
                try {
                    val jsonObject = JSONObject()
                    val json = JSONObject()
                    json.put("jobProperty", propertyArray)
                    json.put("UserId", AppPreferences.getUserData(Params.UserId).toInt())
                    json.put("PropertyMasterId", propertyId)
                    json.put("CustomerMasterId", customerId)
                    json.put("JobVisitingDate", selectedDate)
                    json.put("JobVisitingTime", selectedTime)
                    json.put("Remarks", remark)
                    json.put("JobLanguages", selectedLanguageList.joinToString(separator = ","))
                    jsonObject.put("data", json)
                    Log.d("createJob", json.toString())

                    val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                    val response = ServiceApi.retrofitService.createJob(body)

                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d("createJob", response.code().toString())
                            Log.d("createJob", response.body().toString())

                            if (response.code() == 200) {
                                toast("Job Create Success")
                                finish()
                            } else {
                                toast("please try again")
                            }

                            progressDialog.dialog.dismiss()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "something wrong")
                            Log.d(TAG, "createNewJob: ${response.code()}")
                            Log.d(TAG, "createNewJob: ${response.errorBody()}")
                            Log.d(TAG, "createNewJob: ${response.message()}")
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

        override fun onItemClick(itemPosition: Int, data: AllPropertiesList) {
            propertyId = data.propertyTypeMasterId!!
            selectedList.add(data.id.toString())
            for (i in selectedList.indices) {
                val jsonObject = JSONObject()
                jsonObject.put("PropertyMasterId", selectedList[i].toInt())
                propertyArray.put(jsonObject)
                toast("Selected : " + selectedList)
            }
        }

        override fun onItemClick(itemPosition: Int, language: AllJobLanguageList) {
            if (selectedLanguageList.size > 0) {
                if (selectedLanguageList.contains(language.name!!)) {
                    selectedLanguageList.remove(language.name)
                } else {
                    selectedLanguageList.add(language.name)
                }
            } else {
                selectedLanguageList.add(language.name!!)
            }
        }
    }