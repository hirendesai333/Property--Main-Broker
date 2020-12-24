package com.soboft.propertybroker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.model.PropertyListModel
import com.soboft.propertybroker.utils.Params
import com.soboft.properybroker.listeners.OnPropertyClick

class AllPropertyListAdapter(
    var from: String,
    var list: ArrayList<PropertyListModel>,
    var onPropertyClick: OnPropertyClick
) :
    RecyclerView.Adapter<AllPropertyListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.name)
        val postedBy: TextView = itemView.findViewById(R.id.postedBy)
        val rootLayout: CardView = itemView.findViewById(R.id.rootLayout)
        val bidsOverview: LinearLayout = itemView.findViewById(R.id.bidsOverview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_property_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (from == Params.MY_POSTED_JOBS) {
            holder.postedBy.visibility = View.GONE
            holder.bidsOverview.visibility = View.VISIBLE
        } else {
            holder.bidsOverview.visibility = View.GONE
            holder.postedBy.visibility = View.VISIBLE
        }
        holder.propertyName.text = currentItem.name
        holder.rootLayout.setOnClickListener {
            onPropertyClick.onPropertyClick()
        }
    }

}