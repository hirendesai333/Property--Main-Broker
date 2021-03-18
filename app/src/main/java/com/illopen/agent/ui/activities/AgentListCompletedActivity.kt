package com.illopen.agent.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.illopen.agent.R
import com.illopen.agent.adapters.CompletedAgentAdapter
import com.illopen.agent.databinding.ActivityAgentListCompletedBinding
import com.illopen.agent.listeners.OnAgentListClick
import com.illopen.agent.model.BidListModel

class AgentListCompletedActivity : AppCompatActivity(), OnAgentListClick {

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
        binding.agentListRv.adapter = CompletedAgentAdapter(list, this)
    }

    override fun onRatingClicked(currentItem: BidListModel) {
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.add_rating_popup, null);

        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setView(view)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()

        dialog.show()

        val submit: Button = view.findViewById(R.id.submit)
        submit.setOnClickListener {
            dialog.dismiss()
        }
    }

}