package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.BidListModel
import com.soboft.propertybroker.utils.Params

class BidListAdapter(val subParent: String?, var list: ArrayList<BidListModel>) :
    RecyclerView.Adapter<BidListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val bidAmount: TextView = itemView.findViewById(R.id.bidAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_bid_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (subParent == Params.OTHER_NEW_JOBS) {
            holder.bidAmount.text = "Bid Amount: *****"
        } else {
            holder.bidAmount.text = "Bid Amount: $1500"
        }
        holder.name.text = currentItem.name
    }
}