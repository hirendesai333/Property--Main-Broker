package com.soboft.propertybroker.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.adapters.NotificationAdapter
import com.soboft.propertybroker.databinding.ActivityNotificationBinding
import com.soboft.propertybroker.model.NotificationModel

class Notification : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = ArrayList<NotificationModel>()
        list.add(NotificationModel("New property added"))
        list.add(NotificationModel("Job completed"))
        list.add(NotificationModel("Job started"))
        list.add(NotificationModel("Job completed"))
        list.add(NotificationModel("Job completed"))
        list.add(NotificationModel("New property added"))
        binding.notificationsRv.adapter = NotificationAdapter(list = list)

    }
}