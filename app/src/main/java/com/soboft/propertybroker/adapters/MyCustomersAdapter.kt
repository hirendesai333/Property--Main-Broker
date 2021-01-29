package com.soboft.propertybroker.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.SingleCustomerLayoutBinding
import com.soboft.propertybroker.model.AllCustomerModel
import com.soboft.propertybroker.model.Value

class MyCustomersAdapter(var context: Context, var list : List<Value>, var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MyCustomersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_customer_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]
        views.name.text = currentItem.customerName
        views.email.text = currentItem.customerEmail
        views.mobile.text = currentItem.phoneNumber

        holder.binding.edit.setOnClickListener {
            itemClickListener.onItemClick(position, list[position])
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val binding = SingleCustomerLayoutBinding.bind(itemView)
    }

    interface OnItemClickListener {
        fun onItemClick(itemPosition: Int, data: Value)
    }
}