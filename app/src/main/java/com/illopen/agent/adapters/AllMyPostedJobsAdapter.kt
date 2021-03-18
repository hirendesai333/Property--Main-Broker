package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.MyPostedJobsList

class AllMyPostedJobsAdapter(var from: String,var list: List<MyPostedJobsList>,var itemClickListener : OnItemClickListener)
    : RecyclerView.Adapter<AllMyPostedJobsAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.my_posted_job_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.propertyName.text = currentItem.customerName
        holder.lowBid.text = currentItem.lowestBid.toString()
        holder.highBid.text = currentItem.highestBid.toString()
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
//        holder.isBidAdded.text = currentItem.statusName

        holder.rootLayout.setOnClickListener {
            itemClickListener.onItemClick(position,list[position])
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val propertyName: TextView = itemView.findViewById(R.id.title)
//        val postedBy: TextView = itemView.findViewById(R.id.postedBy)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val lowBid : TextView = itemView.findViewById(R.id.lowBid)
        val highBid : TextView = itemView.findViewById(R.id.highBid)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
//        val bidsOverview: LinearLayout = itemView.findViewById(R.id.bidsOverview)
//        val bidStatusLayout: RelativeLayout = itemView.findViewById(R.id.bidStatusLayout)
    }

    interface OnItemClickListener{
        fun onItemClick(itemPosition : Int,data : MyPostedJobsList)
    }

}