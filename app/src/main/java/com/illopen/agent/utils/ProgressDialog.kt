package com.illopen.agent.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.illopen.agent.R

class ProgressDialog(context: Context) {
    var dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading)
        dialog = builder.create()
    }
}