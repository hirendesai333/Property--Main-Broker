package com.soboft.propertybroker.ui.activities

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.R
import com.soboft.propertybroker.adapters.MyCustomersAdapter
import com.soboft.propertybroker.databinding.ActivityMyCustomersBinding

class MyCustomers : AppCompatActivity() {

    lateinit var binding: ActivityMyCustomersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customerRv.adapter = MyCustomersAdapter()

        binding.addNew.setOnClickListener {
            addNewCustomer()
        }

    }

    private fun addNewCustomer() {
        val mDialog = Dialog(this, R.style.Theme_PropertyMainBroker)
        mDialog.setContentView(R.layout.add_customer_popup)
        mDialog.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)

        mDialog.show()
    }
}