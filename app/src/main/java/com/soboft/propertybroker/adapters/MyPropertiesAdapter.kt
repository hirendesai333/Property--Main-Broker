package com.soboft.propertybroker.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.SingleMyPropertyBinding
import com.soboft.propertybroker.model.Values
import com.soboft.propertybroker.ui.activities.AddProperty
import com.soboft.propertybroker.ui.activities.PropertyMoreDetails

class MyPropertiesAdapter(var context: Context, var list: List<Values>) : RecyclerView.Adapter<MyPropertiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_my_property, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

        views.name.text = currentItem.propertyName.toString()
        views.location.text = currentItem.countryName.toString()
        views.propertyType.text = currentItem.propertyType.toString()
        views.avalibleForSale.text = currentItem.availableFor.toString()
        views.status.text = currentItem.isActive.toString()

        holder.binding.moreDetails.setOnClickListener {
            context.startActivity(Intent(context, PropertyMoreDetails::class.java))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SingleMyPropertyBinding.bind(itemView)
    }

}