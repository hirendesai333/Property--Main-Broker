package com.soboft.propertybroker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soboft.propertybroker.R
import com.soboft.propertybroker.databinding.UserLanguageListBinding
import com.soboft.propertybroker.model.UserLanguage

class AllUserLanguageAdapter(var context: Context,var list: List<UserLanguage>) : RecyclerView.Adapter<AllUserLanguageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.user_language_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val views = holder.binding
       val currentItem = list[position]

        views.userLanguage.setText(currentItem.languageName).toString()
    }

    override fun getItemCount(): Int {
      return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding  = UserLanguageListBinding.bind(itemView)
    }

}