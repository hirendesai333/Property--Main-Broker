package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.AvailableJobs
import com.illopen.agent.model.CompletedJobsAssignList

class MyCompletedJobsAdapter(var from: String, var list: ArrayList<CompletedJobsAssignList>, var itemClickListener : OnCompletedAssignClickListener)
    : RecyclerView.Adapter<MyCompletedJobsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_completed_jobs_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.jobNo.text = "Job No: " + currentItem.jobNo.toString()
        holder.propertyName.text = currentItem.userName
        holder.postedBy.text = "Posted By: " + currentItem.userName
        holder.assignBy.text = "Don By: " + currentItem.assignedUserName
        holder.assignPhone.text = currentItem.assignedPhoneNumber
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.rating.text = currentItem.jobRatting.toString()
        holder.jobstatus.text = currentItem.statusName
        holder.postDate.text = "Posted on: " + currentItem.createdDateStr
        holder.totalProperty.text = "Total Property: " + currentItem.totalProperty.toString()

        holder.rootLayout.setOnClickListener {
            itemClickListener.onItemClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val jobNo : TextView = itemView.findViewById(R.id.jobNo)
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val assignBy : TextView = itemView.findViewById(R.id.assignBy)
        val postedBy: TextView = itemView.findViewById(R.id.postedBy)
        val assignPhone: TextView = itemView.findViewById(R.id.postedPhone)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val postDate : TextView = itemView.findViewById(R.id.postDate)
        val totalProperty : TextView = itemView.findViewById(R.id.totalProperty)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
        val jobstatus : TextView = itemView.findViewById(R.id.jobStatus)
        val ratingLayout: LinearLayout = itemView.findViewById(R.id.ratingLayout)
        val rating : TextView = itemView.findViewById(R.id.myRating)
    }

    interface OnCompletedAssignClickListener{
        fun onItemClick(itemPosition : Int,data : CompletedJobsAssignList)
    }

    fun updateList(ItemList: ArrayList<CompletedJobsAssignList>){
        list = ItemList
        notifyDataSetChanged()
    }
}