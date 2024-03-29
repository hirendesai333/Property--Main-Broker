package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.MyPostedJobList
import com.illopen.agent.model.OngoingMyPostedJobList

class UpComingMyPostedJobAdapter(var from : String, var list: List<OngoingMyPostedJobList>, var itemClick : OnMyPostedJobClick)
    : RecyclerView.Adapter<UpComingMyPostedJobAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_mypostedjob_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

//        if (currentItem.assignedUserName == null)
//        {
//            holder.assignTo.visibility = View.GONE
//            holder.assignPhone.visibility = View.GONE
//        }else{
//            holder.assignTo.visibility = View.VISIBLE
//            holder.assignPhone.visibility = View.VISIBLE
//            holder.assignTo.text = currentItem.assignedUserName.toString()
//            holder.assignPhone.text = currentItem.assignedPhoneNumber.toString()
//        }
        holder.jobNo.text = "Job No: " + currentItem.jobNo.toString()
        holder.propertyName.text = currentItem.userName
        holder.jobStatus.text = currentItem.statusName
        holder.date.text = currentItem.jobVisitingDate
        holder.time.text = currentItem.jobVisitingTime
        holder.created.text = "Posted on: " + currentItem.createdDateStr
        holder.totalProperty.text = "Total Property: " + currentItem.totalProperty.toString()
        holder.assignTo.text = "Assign To: " + currentItem.assignedUserName
        holder.assignPhone.text = currentItem.assignedPhoneNumber
        holder.bidAmount.text = "Bid Amount: $" + currentItem.bidAmount.toString()

        holder.rootLayout.setOnClickListener {
            itemClick.onMyPostedClick(position,list[position])
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val jobNo : TextView = itemView.findViewById(R.id.jobNo)
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val assignTo: TextView = itemView.findViewById(R.id.assignTo)
        val assignPhone: TextView = itemView.findViewById(R.id.assignPhone)
        val time : TextView = itemView.findViewById(R.id.time)
        val date : TextView = itemView.findViewById(R.id.date)
        val created : TextView = itemView.findViewById(R.id.postDate)
        val totalProperty : TextView = itemView.findViewById(R.id.totalProperty)
        val jobStatus: TextView = itemView.findViewById(R.id.jobStatus)
        val bidAmount : TextView = itemView.findViewById(R.id.bidAmount)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
    }

    interface OnMyPostedJobClick{
        fun onMyPostedClick(position: Int,currentItem : OngoingMyPostedJobList)
    }

    fun updateList(ItemList: ArrayList<OngoingMyPostedJobList>){
        list = ItemList
        notifyDataSetChanged()
    }
}