package com.app.kera.preference

import android.content.Context
import android.content.SharedPreferences

class AppSharedPreference constructor(context: Context, var prefName: String) :
    SharedPreference {

     val mPrefs: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    override fun getString(key: String,def:String): String? {
        return mPrefs.getString(key, def)
    }


    override fun saveString(key: String, value: String) {
        mPrefs.edit().putString(key, value).apply()
    }

    override fun getInt(key: String): Int {
        return mPrefs.getInt(key, 0)
    }

    override fun saveInt(key: String, value: Int) {
        mPrefs.edit().putInt(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        mPrefs.edit().putBoolean(key, value).apply()
    }

    override fun clearUserData(token: String, loginData: String) {
        mPrefs.edit().remove(token).remove(loginData).apply()
    }

    override fun logout(): SharedPreferences.Editor {
        return mPrefs.edit()
    }
}
