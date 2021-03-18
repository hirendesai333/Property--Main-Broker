package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.CityListBinding
import com.illopen.agent.model.AllCity

class AllCityListAdapter(var context: Context,var list: List<AllCity>, var itemClickListener : OnItemClickListener)
    : RecyclerView.Adapter<AllCityListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     val view = LayoutInflater.from(parent.context).inflate(R.layout.city_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]
        views.city.text = currentItem.city.toString()

        views.city.setOnClickListener {
            itemClickListener.onItemClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = CityListBinding.bind(itemView)
    }

    interface OnItemClickListener{
        fun onItemClick(itemPosition : Int,data : AllCity)
    }

}