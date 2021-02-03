package com.soboft.propertybroker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    lateinit var PREFERENCE: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    fun initialize(context: Context) {
        PREFERENCE = context.getSharedPreferences(Params.PREF, Context.MODE_PRIVATE)
        editor = PREFERENCE.edit()!!
    }

    fun setUserData(key: String , value : String){
        editor.putString(key, value)
        editor.apply()
    }

    fun getUserData(key : String) : String{
        return PREFERENCE.getString(key,"")!!
    }


    fun logOut() {
        editor.clear()
        editor.commit()
        editor.apply()
    }
}