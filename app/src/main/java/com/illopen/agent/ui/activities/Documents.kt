package com.illopen.agent.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.illopen.agent.adapters.DocumentsAdapter
import com.illopen.agent.databinding.ActivityDocumentsBinding
import com.illopen.agent.model.DocumentsModel

class Documents : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentsBinding

    internal var dropDownValue = arrayOf("SSN")
    private lateinit var dropDownAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = ArrayList<DocumentsModel>()
        list.add(DocumentsModel("yes", "Notice"))
        list.add(DocumentsModel("no", "Notice new"))
        list.add(DocumentsModel("yes", "Municipality bill"))
        list.add(DocumentsModel("yes", "Land paper"))
        list.add(DocumentsModel("yes", "Owner document"))
        binding.documentRv.adapter = DocumentsAdapter(this, list = list)

        dropDownAdapter = object :
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dropDownValue) {}
        binding.filter.adapter = dropDownAdapter
    }
}