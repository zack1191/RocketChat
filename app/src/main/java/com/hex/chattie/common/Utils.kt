package com.hex.chattie.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.view.Menu
import androidx.annotation.MenuRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import com.hex.chattie.R

class Utils
{
    fun isNetworkConnected(context : Context) : Boolean?
    {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun savePreference(context : Context, key : String, value : String)
    {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPref !!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    fun deletePreference(context : Context, key : String)
    {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPref !!.edit()
        editor.remove(key)
        editor.commit()
    }

    fun getPreference(context : Context, key : String) : String?
    {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    @SuppressLint("RestrictedApi")
    fun Context.inflate(@MenuRes menuRes: Int): Menu
    {
        val menu = MenuBuilder(this)
        val menuInflater = SupportMenuInflater(this)
        menuInflater.inflate(menuRes, menu)
        return menu
    }
}