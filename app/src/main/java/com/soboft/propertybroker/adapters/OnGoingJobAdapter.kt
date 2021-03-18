package com.soboft.propertybroker.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.OngoingJobItemBinding
import com.soboft.propertybroker.model.JobPropertyList
import com.soboft.propertybroker.ui.activities.PropertyDetail

class OnGoingJobAdapter(var context: Context,var list : List<JobPropertyList>, var onGoingJobPropertyClick: JobPropertyClick)
    : RecyclerView.Adapter<OnGoingJobAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val propertyType : TextView = itemView.findViewById(R.id.propertyType)
        val address : TextView = itemView.findViewById(R.id.address)
        val mark : TextView = itemView.findViewById(R.id.mark)
        val review : TextView = itemView.findViewById(R.id.review)
        val rootLayout : CardView = itemView.findViewById(R.id.rootLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_job_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        if (currentItem.review.isNullOrEmpty()){

            holder.mark.visibility = View.GONE
            holder.review.visibility = View.GONE
        }else{

            holder.review.visibility = View.VISIBLE
            holder.mark.visibility = View.VISIBLE
        }

        holder.name.text = currentItem.propertyName
        holder.address.text = currentItem.propertyAddress
        holder.propertyType.text = currentItem.propertyTypeName

        holder.rootLayout.setOnClickListener {
            val intent =  Intent(context, PropertyDetail::class.java)
            intent.putExtra("propertyMasterId",currentItem.propertyMasterId.toString())
            context.startActivity(intent)
        }

        holder.review.setOnClickListener {
            onGoingJobPropertyClick.onGoingJobPropertyClick(position,list[position])
        }

        holder.mark.setOnClickListener {
            onGoingJobPropertyClick.onGoingJobPropertyClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface JobPropertyClick{
        fun onGoingJobPropertyClick(position: Int,currentItem : JobPropertyList)
    }
}