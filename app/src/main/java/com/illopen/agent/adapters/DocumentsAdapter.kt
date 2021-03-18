package com.illopen.agent.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.model.DocumentsModel

class DocumentsAdapter(var context: Context, var list: ArrayList<DocumentsModel>) :
    RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val approval: TextView = itemView.findViewById(R.id.approval)
        val type: TextView = itemView.findViewById(R.id.type)
        val view: TextView = itemView.findViewById(R.id.view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_document_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        if (currentItem.approval == "yes") {
            holder.approval.text = "Approved"
        } else {
            holder.approval.text = "Pending"
        }
        holder.type.text = currentItem.type
        holder.view.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.africau.edu/images/default/sample.pdf")
            )
            startActivity(context, browserIntent, null)
        }
    }

}