package com.illopen.agent.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.NotificationAdapter
import com.illopen.agent.databinding.ActivityNotificationBinding
import com.illopen.agent.model.NotificationList
import com.illopen.agent.model.Value
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import kotlinx.coroutines.*

class Notification : AppCompatActivity() {

    private val TAG = "Notification"

    private lateinit var binding: ActivityNotificationBinding

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val list = ArrayList<NotificationModel>()
//        list.add(NotificationModel("New property added"))
//        list.add(NotificationModel("Job completed"))
//        list.add(NotificationModel("Job started"))
//        list.add(NotificationModel("Job completed"))
//        list.add(NotificationModel("Job completed"))
//        list.add(NotificationModel("New property added"))
//        binding.notificationsRv.adapter = NotificationAdapter(list = list)

        binding.title.setOnClickListener { onBackPressed() }

        getNotification()
    }

    private fun getNotification() {
        coroutineScope.launch {
            try {
                val map = HashMap<String, String>()
                map["Offset"] = "0"
                map["Limit"] = "0"
                map["Page"] = "0"
                map["PageSize"] = "0"
                map["TotalCount"] = "0"

                val response = ServiceApi.retrofitService.notification(
                    AppPreferences.getUserData(Params.UserId).toInt(),map
                )
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){

                        Log.d("getNotification", response.code().toString())
                        Log.d("getNotification",response.body().toString())

                        val list : List<NotificationList> = response.body()!!.values!!

                        binding.notificationsRv.adapter = NotificationAdapter(this@Notification,list)

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