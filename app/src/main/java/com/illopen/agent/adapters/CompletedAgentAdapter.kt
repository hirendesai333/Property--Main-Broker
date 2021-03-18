package com.illopen.agent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.listeners.OnAgentListClick
import com.illopen.agent.model.BidListModel

class CompletedAgentAdapter(var list: ArrayList<BidListModel>, var onAgentListClick: OnAgentListClick) :
    RecyclerView.Adapter<CompletedAgentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val ratingLayout: LinearLayout = itemView.findViewById(R.id.ratingLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_agent_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.name.text = currentItem.name
        holder.ratingLayout.setOnClickListener {
            onAgentListClick.onRatingClicked(currentItem)
        }
    }
}