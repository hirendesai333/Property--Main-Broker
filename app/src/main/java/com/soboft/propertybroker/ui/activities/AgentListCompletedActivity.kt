package com.soboft.propertybroker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.BidListAdapter
import com.soboft.propertybroker.adapters.CompletedAgentAdapter
import com.soboft.propertybroker.databinding.ActivityAgentListCompletedBinding
import com.soboft.propertybroker.model.BidListModel

class AgentListCompletedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentListCompletedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgentListCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val list = ArrayList<BidListModel>()
        list.add(BidListModel("Shah property broker"))
        list.add(BidListModel("Kenils - property broker"))
        list.add(BidListModel("OM estate broker"))
        list.add(BidListModel("Divya Estate Management"))
        list.add(BidListModel("R K Broker"))
        list.add(BidListModel("Sweet Home Estate Management"))
        binding.agentListRv.adapter = CompletedAgentAdapter(list)
    }
}