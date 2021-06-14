package com.illopen.agent.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.illopen.agent.R
import com.illopen.agent.databinding.UserLanguageListBinding
import com.illopen.agent.model.SelectedLanguageModel
import com.illopen.agent.ui.activities.MainActivity
import com.illopen.agent.ui.activities.Profile
import kotlinx.android.synthetic.main.user_language_list.view.*


class AllJobLanguagesAdapter(
    var context: Context,
    var list: ArrayList<SelectedLanguageModel>,
    var applicationContext: Context,
    var profile: Profile
) :
    RecyclerView.Adapter<AllJobLanguagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_language_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val views = holder.binding
        val currentItem = list[position]

//        if (currentItem.isSelected!!) {
//            views.userLanguage.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_700))
//            views.userLanguage.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
//            views.userLanguage.text = currentItem.languageName
//        } else {
//            views.userLanguage.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_grey))
//            views.userLanguage.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)))
//            views.userLanguage.text = currentItem.languageName
//        }

        views.userLanguage.setOnClickListener { view ->
            if (view.userLanguage.isSelected) {
                (applicationContext as Profile).selectArray(currentItem)
            } else {
                if (view.userLanguage.isSelected) {
                    (applicationContext as Profile).selectArray(currentItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = UserLanguageListBinding.bind(itemView)
    }

}