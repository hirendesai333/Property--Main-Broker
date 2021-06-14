package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.CompletedJobAssignItemBinding
import com.illopen.agent.model.JobPropertyList
import com.illopen.agent.ui.activities.PropertyDetail

class CompletedJobAssignDetailsAdapter(var context: Context, var list: List<JobPropertyList>)
    : RecyclerView.Adapter<CompletedJobAssignDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_job_assign_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val view = holder.binding
        val currentItem = list[position]

        view.name.text = currentItem.propertyName
        view.address.text = "Address: " + currentItem.propertyAddress
        view.availableFor.text = "Available for: " + currentItem.availableForMasterName
        view.propertyType.text = "Property type: " + currentItem.propertyTypeName
        view.propertyRating.rating = currentItem.rating!!.toFloat()
        view.price.text = "Price: $" + currentItem.propertyPrice

        view.review.text = "Review: " + currentItem.review
        view.note.text = "Note: " + currentItem.note

        view.bidAmount.text = "Bid Amount: " + currentItem.bidAmount
        view.bidnote.text = "Note: " + currentItem.bidNote

        view.rootLayout.setOnClickListener {
            val intent =  Intent(context, PropertyDetail::class.java)
            intent.putExtra("propertyMasterId",currentItem.propertyMasterId.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
      return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = CompletedJobAssignItemBinding.bind(itemView)
    }
}