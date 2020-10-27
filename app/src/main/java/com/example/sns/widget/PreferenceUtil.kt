package com.example.sns.widget

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getEmail( defValue: String): String {
        return prefs.getString("email", defValue).toString()
    }

    fun setEmail(str: String?) {
        prefs.edit().putString("email", str).apply()
    }

    fun getToken(defValue: String): String {
        return prefs.getString("token", defValue).toString()
    }

    fun setToken(str: String?) {
        prefs.edit().putString("token", str).apply()
    }

    fun getUsername(defValue: String): String {
        return prefs.getString("userName", defValue).toString()
    }

    fun setUsername(str: String?) {
        prefs.edit().putString("userName", str).apply()
    }

    fun setCheckLogin( str: String?) {
        prefs.edit().putString("checkLogin", str).apply()
    }

    fun getCheckLogin(defValue: String): String {
        return prefs.getString("checkLogin", defValue).toString()
    }

    fun setCheckPermission(bol: Boolean) {
        prefs.edit().putBoolean("permission", bol).apply()
    }

    fun getCheckPermission(defValue: Boolean): Boolean {
        return prefs.getBoolean("permission", defValue)
    }

    fun getLastTime(defValue: Long): Long {
        return prefs.getLong("lastConnectTime", defValue)
    }

    fun setLastTime(long: Long?) {
        prefs.edit().putLong("lastConnectTime", long!!).apply()
    }
}