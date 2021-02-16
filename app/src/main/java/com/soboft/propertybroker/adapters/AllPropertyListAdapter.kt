package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.AvailableJobs
import com.soboft.propertybroker.utils.Params
import com.soboft.propertybroker.listeners.OnNewJobsClick

class AllPropertyListAdapter(
    var from: String,
    var list: List<AvailableJobs>,
    var onPropertyClick: OnNewJobsClick
    ) : RecyclerView.Adapter<AllPropertyListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val postedBy: TextView = itemView.findViewById(R.id.postedBy)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
        val lowBid : TextView = itemView.findViewById(R.id.lowBid)
        val highBid : TextView = itemView.findViewById(R.id.highBid)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
        val bidsOverview: LinearLayout = itemView.findViewById(R.id.bidsOverview)
        val isBidAdded: TextView = itemView.findViewById(R.id.isBidAdded)
        val bidStatusLayout: RelativeLayout = itemView.findViewById(R.id.bidStatusLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_property_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (from == Params.MY_POSTED_JOBS) {
            holder.postedBy.visibility = View.GONE
            holder.bidStatusLayout.visibility = View.GONE
            holder.bidsOverview.visibility = View.VISIBLE
        } else {
            holder.bidsOverview.visibility = View.GONE
            holder.postedBy.visibility = View.VISIBLE
            holder.bidStatusLayout.visibility = View.VISIBLE
//            if (currentItem.isBidAdded == 1) {
//                holder.isBidAdded.text = "Bid Added"
//                holder.isBidAdded.visibility = View.VISIBLE
//            } else {
//                holder.isBidAdded.visibility = View.GONE
//            }
        }
        holder.propertyName.text = currentItem.customerName
        holder.lowBid.text = currentItem.lowestBid.toString()
        holder.highBid.text = currentItem.highestBid.toString()
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.isBidAdded.text = currentItem.statusName

        holder.rootLayout.setOnClickListener {
            onPropertyClick.onNewJobsClick(currentItem)
        }
    }

}