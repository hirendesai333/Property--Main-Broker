package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.ui.activities.PropertyDetail

class OnGoingJobAdapter(var context: Context,var list : List<JobPropertyList>,
                        var onGoingJobReviewClick: JobPropertyReviewClick)
    : RecyclerView.Adapter<OnGoingJobAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val availableFor : TextView = itemView.findViewById(R.id.availableFor)
        val price : TextView = itemView.findViewById(R.id.price)
        val propertyType : TextView = itemView.findViewById(R.id.propertyType)
        val address : TextView = itemView.findViewById(R.id.address)
//        val mark : TextView = itemView.findViewById(R.id.mark)
        val review : TextView = itemView.findViewById(R.id.review)
        val rootLayout : CardView = itemView.findViewById(R.id.rootLayout)

        val bidAmount : TextView = itemView.findViewById(R.id.bidAmount)
        val bidNote : TextView = itemView.findViewById(R.id.note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_job_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

//        if (currentItem.review.isNullOrEmpty()){
//
//            holder.mark.visibility = View.GONE
//            holder.review.visibility = View.GONE
//        }else{
//
//            holder.review.visibility = View.VISIBLE
//            holder.mark.visibility = View.VISIBLE
//        }

        holder.name.text = currentItem.propertyName
        holder.availableFor.text = "Available for: " + currentItem.availableForMasterName
        holder.price.text = "Price: $" + currentItem.propertyPrice
        holder.address.text = "Address: " + currentItem.propertyAddress
        holder.propertyType.text = "Property type: " + currentItem.propertyTypeName
        holder.bidAmount.text = "Bid Amount: " + currentItem.bidAmount
        holder.bidNote.text = "Note: " + currentItem.bidNote

        holder.rootLayout.setOnClickListener {
            val intent =  Intent(context, PropertyDetail::class.java)
            intent.putExtra("propertyMasterId",currentItem.propertyMasterId.toString())
            context.startActivity(intent)
        }

        holder.review.setOnClickListener {
            onGoingJobReviewClick.onGoingJobReviewClick(position,list[position])
        }

        if (currentItem.rating!! > 0) {
            holder.review.text = "Update Review"
        } else {
            holder.review.text = "Review"
        }

//        holder.mark.setOnClickListener {
//            onGoingJobMarkClick.onGoingJobMarkClick(position,list[position])
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface JobPropertyReviewClick{
        fun onGoingJobReviewClick(position: Int,currentItem : JobPropertyList)
    }
}