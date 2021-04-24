package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.CompletedMyPostedJobsList
import com.illopen.agent.model.JobPropertyList

class CompletedJobsAdapter(var from: String, var list: List<CompletedMyPostedJobsList>,
                           var onPropertyClick: OnCompletedJobClick,
                           var markerClick : OnMarkerClick) :
    RecyclerView.Adapter<CompletedJobsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_jobs_single_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

//        holder.postedBy.text = "To: Rudra Pvt Ltd."
        holder.propertyName.text = currentItem.userName
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.rating.text = currentItem.jobRatting.toString()
//        holder.jobStatus.text = currentItem.statusName
        holder.createdDate.text = "Posted on: " + currentItem.createdDateStr
        holder.totalProperty.text = "Total Property: " + currentItem.totalProperty.toString()

        holder.rootLayout.setOnClickListener {
            onPropertyClick.onCompletedJobsClick(currentItem)
        }

        holder.markedCompleted.setOnClickListener {
            markerClick.onCompletedMarkerClick(currentItem)
        }

//        holder.ratingBtn.setOnClickListener {
//            reviewItemClick.onClickRating(position, list[position])
//        }

//        if (currentItem.jobRatting!! > 0){
//            holder.ratingBtn.text = "Update Review"
//        }else{
//            holder.ratingBtn.text = "Review"
//        }

        if (currentItem.statusMasterId!! > 3){
            holder.markedCompleted.text = "Completed Job"
        }else{
            holder.markedCompleted.text = "Mark As: Completed "
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val createdDate : TextView = itemView.findViewById(R.id.postDate)
        val totalProperty : TextView = itemView.findViewById(R.id.totalProperty)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
//        val postedBy: TextView = itemView.findViewById(R.id.postedBy)
        val jobStatus : TextView = itemView.findViewById(R.id.jobStatus)
        val markedCompleted : TextView = itemView.findViewById(R.id.markCompleted)
        val ratingLayout: LinearLayout = itemView.findViewById(R.id.ratingLayout)
        val rating : TextView = itemView.findViewById(R.id.myRating)
//        val ratingBtn : Button = itemView.findViewById(R.id.ratingBtn)
    }

    interface OnCompletedJobClick {
        fun onCompletedJobsClick(currentItem: CompletedMyPostedJobsList)
    }

    interface OnMarkerClick{
        fun onCompletedMarkerClick(currentItem : CompletedMyPostedJobsList)
    }

//    interface OnClickRating{
//        fun onClickRating(position: Int,currentItem : CompletedMyPostedJobsList)
//    }
}