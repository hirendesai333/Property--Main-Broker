package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.PropertyImageModel
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.properybroker.listeners.OnPropertyClick

class ProperyImagesListAdapter(val list: ArrayList<PropertyImageModel>) :
    RecyclerView.Adapter<ProperyImagesListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyImage: ImageView = itemView.findViewById(R.id.propertyImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_propety_add_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
//        holder.propertyImage.load(currentItem.image)
    }

}