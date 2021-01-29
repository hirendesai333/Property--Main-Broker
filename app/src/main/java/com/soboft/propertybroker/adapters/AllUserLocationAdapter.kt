package com.soboft.propertybroker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.UserLocationListBinding
import com.soboft.propertybroker.model.UserLocation

class AllUserLocationAdapter(var context: Context, var list: List<UserLocation>) : RecyclerView.Adapter<AllUserLocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.user_location_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val views = holder.binding
      val currentItem = list[position]

      views.userLocation.text = currentItem.cityName.toString().trim()

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val binding = UserLocationListBinding.bind(itemView)
    }
}