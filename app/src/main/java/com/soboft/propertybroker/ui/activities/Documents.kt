package com.soboft.propertybroker.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soboft.propertybroker.adapters.DocumentsAdapter
import com.soboft.propertybroker.databinding.ActivityDocumentsBinding
import com.soboft.propertybroker.model.DocumentsModel

class Documents : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentsBinding

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
    }
}