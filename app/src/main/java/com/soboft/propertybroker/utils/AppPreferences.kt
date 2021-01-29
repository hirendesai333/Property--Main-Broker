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

//    fun setId(key: String, value: Int?) {
//        editor.putInt(key, value!!)
//        editor.apply()
//    }
//
//    fun getId(key: String): Int {
//        return PREFERENCE.getInt(key, 0)
//    }
//
//    fun setUserTypeMasterId(key: String, value: Int?) {
//        editor.putInt(key, value!!)
//        editor.apply()
//    }
//
//    fun getUserTypeMasterId(key: String): Int {
//        return PREFERENCE.getInt(key, 0)
//    }
//
//    fun setUsername(key: String, value: String) {
//        editor.putString(key, value)
//        editor.apply()
//    }
//
//    fun getUsername(key: String): String {
//        return PREFERENCE.getString(key, "")!!
//    }
//
//    fun setEmail(key: String, value: String) {
//        editor.putString(key, value)
//        editor.apply()
//    }
//
//    fun getEmail(key: String): String {
//        return PREFERENCE.getString(key, "")!!
//    }
//
//    fun setMobileNumber(key: String, value: String) {
//        editor.putString(key, value)
//        editor.apply()
//    }
//
//    fun getMobileNumber(key: String): String {
//        return PREFERENCE.getString(key, "")!!
//    }
//
//    fun setProfileUrl(key: String, value: String) {
//        editor.putString(key, value)
//        editor.apply()
//    }
//
//    fun getProfileUrl(key: String): String {
//        return PREFERENCE.getString(key, "")!!
//    }

    fun logOut() {
        editor.clear()
        editor.commit()
        editor.apply()
    }
}