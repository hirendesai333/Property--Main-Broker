package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.illopen.agent.R
import com.illopen.agent.databinding.SinglePropetyAddImageBinding
import com.illopen.agent.model.PropertyImageList

class ProperyImagesListAdapter(var context: Context, var list: List<PropertyImageList>, var itemDeleteClick : OnItemClickListener) :
    RecyclerView.Adapter<ProperyImagesListAdapter.ViewHolder>() {

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

        holder.binding.apply {

            val currentItem = list[position]
            propertyImage.load(currentItem.urlStr)

            holder.binding.deleteImage.setOnClickListener {
                itemDeleteClick.onItemDeleteClick(position, list[position])
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SinglePropetyAddImageBinding.bind(itemView)
    }

    interface OnItemClickListener{
        fun onItemDeleteClick(itemPosition : Int, deleteImage : PropertyImageList)
    }
}