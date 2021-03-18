package com.illopen.agent.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.BidListAdapter
import com.illopen.agent.databinding.ActivityBidsListBinding
import com.illopen.agent.model.BidListModel
import com.illopen.agent.utils.Params

class BidsList : AppCompatActivity() {

    private lateinit var bindig: ActivityBidsListBinding
    private var subParent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig = ActivityBidsListBinding.inflate(layoutInflater)
        setContentView(bindig.root)

        subParent = subParent.let {
            intent.getStringExtra(Params.SUB_FROM)
        }

        bindig.back.setOnClickListener {
            onBackPressed()
        }

        val list = ArrayList<BidListModel>()
        list.add(BidListModel("Shah property broker"))
        list.add(BidListModel("Kenils - property broker"))
        list.add(BidListModel("OM estate broker"))
        list.add(BidListModel("Divya Estate Management"))
        list.add(BidListModel("R K Broker"))
        list.add(BidListModel("Sweet Home Estate Management"))
        bindig.bidListRv.adapter = BidListAdapter(subParent, list)
    }
}