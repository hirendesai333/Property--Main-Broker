package com.illopen.agent.ui.activities

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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class NewJob : AppCompatActivity(), ChoosePropertyAdapter.OnItemClickListener, JobLanguagesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityNewJob2Binding

    private val TAG = "CreateNewJob"

    private var selectedList: ArrayList<String> = ArrayList()
    private var selectedLanguageList: ArrayList<String> = ArrayList()

    private lateinit var progressDialog: ProgressDialog

    private var languageArray = JSONArray()

    private var propertyId: Int = 0
    private var customerId: Int = 0

    private lateinit var propertyDialog: Dialog

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    lateinit var timePicker: TimePickerDialog
    lateinit var datePicker: DatePickerDialog

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private var customerListValue: ArrayList<Value> = ArrayList()
    private lateinit var customerListAdapterSpinner: ArrayAdapter<String>
    private var propertyArray = JSONArray()

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

            val builder = MaterialDatePicker.Builder.datePicker()
            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val month = now.get(Calendar.MONTH)
            val day = now.get(Calendar.DAY_OF_MONTH)


//            builder.setSelection(now.timeInMillis)
//            picker.show(supportFragmentManager, picker.toString())
//            picker.addOnPositiveButtonClickListener { binding.date.text = (picker.headerText) }

            datePicker = DatePickerDialog(
                this,
                { _, _, monthOfYear, dayOfMonth ->
                    selectedDate =
                        year.toString() + "/" + (monthOfYear + 1).toString() + "/" + dayOfMonth.toString()
                    binding.date.text = selectedDate
                },
                year,
                month,
                day
            )
            datePicker.show()

//            val noww = Date()
//                val calendarMax = Calendar.getInstance()
//                calendarMax.time = Date(noww.time + TimeUnit.DAYS.toMillis(3)) // Set max now + 3 days
//                val maxDate = calendarMax.time
//                SingleDateAndTimePickerDialog.Builder(this)
//                    .bottomSheet()
//                    .defaultDate(maxDate)
//                    .mustBeOnFuture()
//                    .curved()
//                    .displayMinutes(false)
//                    .displayHours(false)
//                    .displayMonth(true)
//                    .displayYears(true)
//                    .displayDaysOfMonth(true)
//                    .displayMonthNumbers(true)
//                    .displayDays(false)
//                    .title("Choose Date")
//                    .listener {
//                        // date format to show user
//                        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
//                        val selectedDate: String = dateFormat.format(it).toString()
//                        val currentDate = Date()
//                        val checkDate = dateFormat.parse(selectedDate)
//                         selectedDate =  binding.date.setText(selectedDate)
//                    }
//                    .display()

        }

        binding.time.setOnClickListener {

//            val materialTimePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).build()
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val min = now.get(Calendar.MINUTE)

            timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    selectedTime =
                        String.format("%d:%d", hourOfDay, minute)
                    binding.time.text = selectedTime
                },
                hour,
                min,
                false
            )
            timePicker.show()

//            materialTimePicker.show(supportFragmentManager, "Select Time")
//
//            materialTimePicker.addOnPositiveButtonClickListener {
//                binding.time.text = SimpleDateFormat("HH:mm").format(now.time)
//            }

//            SingleDateAndTimePickerDialog.Builder(this)
//                .bottomSheet()
//                .curved()
//                .displayMinutes(true)
//                .displayHours(true)
//                .displayMonth(false)
//                .displayYears(false)
//                .displayDaysOfMonth(false)
//                .displayMonthNumbers(false)
//                .displayDays(false)
//                .title("Choose time")
//                .listener {
//
//                    val dateFormat: DateFormat = SimpleDateFormat("hh:mm aa")
//                    val dateString: String = dateFormat.format(it).toString()
//                    selectedTime = binding.time.setText(dateString).toString()
//
//                    val dateFormatNew: DateFormat = SimpleDateFormat("HH:mm:ss")
//                    val dateStringToPass: String = dateFormatNew.format(it).toString()
//                }
//                .display()
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

                        binding.jobLanguage.adapter = JobLanguagesAdapter(this@NewJob, list, this@NewJob)
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
                                    customerId = customerListValue[position].id!!.toInt()
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

        val remark = binding.remark.text.toString().trim()

        if (remark.isEmpty()) {
            binding.remark.error = "Remark is Required"
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
        propertyId = data.propertyTypeMasterId!!.toInt()
        selectedList.clear()
        selectedList.add(data.id.toString())
        for (i in selectedList.indices) {
            val jsonObject = JSONObject()
            jsonObject.put("PropertyMasterId", selectedList[i].toInt())
            propertyArray.put(jsonObject)
            toast("Selected : ")
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