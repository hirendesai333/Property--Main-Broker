package com.soboft.propertybroker.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.databinding.ActivityNewJob2Binding
import com.soboft.properybroker.utils.toast

class NewJob : AppCompatActivity() {

    private lateinit var binding: ActivityNewJob2Binding

    internal var propertyListValue = arrayOf("Shivalik Shilp", "Aditya Prime", "Saujanya 2")
    private lateinit var propertyListAdapterSpinner: ArrayAdapter<String>

    internal var customerListValue = arrayOf("John Doe", "Little John Doe", "Big John Doe")
    private lateinit var customerListAdapterSpinner: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJob2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        propertyListAdapterSpinner = object :
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, propertyListValue) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                (v as TextView).textSize = 14.0F
                return v
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getDropDownView(position, convertView, parent)
                (v as TextView).textSize = 14.0F
                return v
            }
        }
        binding.propertySpinner.adapter = propertyListAdapterSpinner

        customerListAdapterSpinner = object :
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customerListValue) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                (v as TextView).textSize = 14.0F
                return v
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getDropDownView(position, convertView, parent)
                (v as TextView).textSize = 14.0F
                return v
            }
        }

        binding.customerSpinner.adapter = customerListAdapterSpinner

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

        binding.createJob.setOnClickListener {
            toast("New job created successfully")
        }
    }
}