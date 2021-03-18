package com.illopen.agent.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.NotificationAdapter
import com.illopen.agent.databinding.ActivityNotificationBinding
import com.illopen.agent.model.NotificationModel

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