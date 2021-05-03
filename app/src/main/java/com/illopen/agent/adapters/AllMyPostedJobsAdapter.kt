package com.illopen.agent.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.MyPostedJobList

class AllMyPostedJobsAdapter(var from: String, var list: List<MyPostedJobList>, var itemClickListener : OnItemClickListener)
    : RecyclerView.Adapter<AllMyPostedJobsAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.my_posted_job_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.jobNo.text = "Job No: " + currentItem.jobNo.toString()
        holder.propertyName.text = currentItem.customerName
        holder.lowBid.text = "Lowest $" + currentItem.lowestBid.toString()
        holder.highBid.text = "Highest $" + currentItem.highestBid.toString()
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.postDate.text = "Posted on: " + currentItem.createdDateStr
        holder.totalProperty.text = "Total Property: " + currentItem.totalProperty
        holder.rootLayout.setOnClickListener {
            itemClickListener.onItemClick(position,list[position])
        }

        holder.call.setOnClickListener {
            itemClickListener.onCallClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val jobNo : TextView = itemView.findViewById(R.id.jobNo)
        val propertyName: TextView = itemView.findViewById(R.id.title)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val lowBid : TextView = itemView.findViewById(R.id.lowBid)
        val highBid : TextView = itemView.findViewById(R.id.highBid)
        val postDate : TextView = itemView.findViewById(R.id.postDate)
        val totalProperty : TextView = itemView.findViewById(R.id.totalProperty)
        val call : ImageView = itemView.findViewById(R.id.call)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
    }

    interface OnItemClickListener{
        fun onItemClick(itemPosition : Int,data : MyPostedJobList)
        fun onCallClick(itemPosition : Int,data : MyPostedJobList)
    }

}