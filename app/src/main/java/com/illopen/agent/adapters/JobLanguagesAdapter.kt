package com.illopen.agent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.UserLanguageListBinding
import com.illopen.agent.model.AllJobLanguageList

class JobLanguagesAdapter(var context: Context,var list: List<AllJobLanguageList>)
    : RecyclerView.Adapter<JobLanguagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_language_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

        views.userLanguage.text = currentItem.name

//        views.userLanguage.setOnClickListener {
//            itemClick.onItemClick(position,list[position])
//        }
    }

    override fun getItemCount(): Int {
      return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding  = UserLanguageListBinding.bind(itemView)
    }

    interface OnItemClickListener{
        fun onItemClick(itemPosition: Int, language: AllJobLanguageList)
    }
}