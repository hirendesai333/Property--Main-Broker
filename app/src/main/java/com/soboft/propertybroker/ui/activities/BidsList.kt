package com.soboft.propertybroker.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.adapters.BidListAdapter
import com.soboft.propertybroker.databinding.ActivityBidsListBinding
import com.soboft.propertybroker.model.BidListModel

class BidsList : AppCompatActivity() {

    private lateinit var bindig: ActivityBidsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig = ActivityBidsListBinding.inflate(layoutInflater)
        setContentView(bindig.root)

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
        bindig.bidListRv.adapter = BidListAdapter(list)
    }
}