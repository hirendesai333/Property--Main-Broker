package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.AllPropertiesList

class ChoosePropertyAdapter(var context: Context, var list: List<AllPropertiesList>, var itemClick : OnItemClickListener) :
    RecyclerView.Adapter<ChoosePropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.property_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.propertyName.text = currentItem.propertyName.toString()
        holder.address.text = "Address: " + currentItem.propertyAddress.toString()
        holder.pincode.text = "Pincode: " + currentItem.pincode.toString()

        holder.checked.setOnCheckedChangeListener(null)
//        holder.checked.setOnCheckedChangeListener { buttonView, isChecked ->
//            holder.checked.isChecked = isChecked
//            itemClick.onItemClick(position,list[position])
//        }

        holder.checked.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.checked.isChecked = true
                itemClick.onItemClick(position, list[position])
            }else{
                holder.checked.isChecked = false
                itemClick.onItemClick(position, list[position])
            }
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val propertyName : TextView = itemView.findViewById(R.id.property)
        val address : TextView = itemView.findViewById(R.id.address)
        val pincode : TextView = itemView.findViewById(R.id.pincode)
        val checked : CheckBox = itemView.findViewById(R.id.checkBox)
    }

    interface OnItemClickListener{
        fun onItemClick(itemPosition : Int,data : AllPropertiesList)
    }
}