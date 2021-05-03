package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.SingleMyPropertyBinding
import com.illopen.agent.model.AllPropertiesList
import com.illopen.agent.ui.activities.AddProperty
import com.illopen.agent.ui.activities.PropertyMoreDetails

class MyPropertiesAdapter(var context: Context, var list: List<AllPropertiesList>, var itemClickListener : OnItemClickListner)
    : RecyclerView.Adapter<MyPropertiesAdapter.ViewHolder>() {

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
            Intent(context,PropertyMoreDetails::class.java).apply {
                putExtra("propertyName",currentItem.propertyName)
                putExtra("PropertyMasterId",currentItem.id)
                context.startActivity(this)
            }
        }

        holder.binding.edit.setOnClickListener {
            Intent(context, AddProperty::class.java).apply {
                putExtra("propertyId", currentItem.id.toString())
                putExtra("from", "edit")
                context.startActivity(this)
            }
        }

        holder.binding.delete.setOnClickListener {
            itemClickListener.onItemClick(position,list[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SingleMyPropertyBinding.bind(itemView)
    }

    interface OnItemClickListner{
        fun onItemClick(position: Int, data : AllPropertiesList)
    }

}