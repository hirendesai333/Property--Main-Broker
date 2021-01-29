package com.soboft.propertybroker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.StateListBinding
import com.soboft.propertybroker.model.State

class AllStateListAdapter(var context: Context, var list: List<State>, var itemClickListener : OnItemClickListner) : RecyclerView.Adapter<AllStateListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.state_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

        views.state.text = currentItem.state

        views.state.setOnClickListener {

            itemClickListener.onItemClick(position,list[position])
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding  = StateListBinding.bind(itemView)
    }

    interface OnItemClickListner{
        fun onItemClick(position: Int, data : State)
    }
}