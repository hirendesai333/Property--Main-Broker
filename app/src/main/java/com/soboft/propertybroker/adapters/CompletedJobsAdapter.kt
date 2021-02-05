package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.AllCompletedJobsList
import com.soboft.propertybroker.utils.Params
import com.soboft.propertybroker.listeners.OnCompletedJobClick

class CompletedJobsAdapter(var from: String, var list: List<AllCompletedJobsList>, var onPropertyClick: OnCompletedJobClick) :
    RecyclerView.Adapter<CompletedJobsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.completed_jobs_single_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (from == Params.MY_COMPLETED_JOBS) {
            holder.postedBy.text = "By: Rudra Pvt Ltd."
        } else {
            holder.postedBy.text = "To: Rudra Pvt Ltd."
        }
        holder.propertyName.text = currentItem.userName
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.rating.text = currentItem.averageRatting.toString()
        holder.rootLayout.setOnClickListener {
            onPropertyClick.onCompletedJobsClick(currentItem)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
        val postedBy: TextView = itemView.findViewById(R.id.postedBy)
        val ratingLayout: LinearLayout = itemView.findViewById(R.id.ratingLayout)
        val rating : TextView = itemView.findViewById(R.id.myRating)
    }
}