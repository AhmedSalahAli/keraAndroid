package com.example.kera.preference

import android.content.SharedPreferences

interface SharedPreference {
    // Adding all CRUD options for Shared Preferences
    fun getString(key: String): String?

    fun saveString(key: String, value: String)

    fun getInt(key: String): Int

    fun saveInt(key: String, value: Int)

    fun getBoolean(key: String): Boolean

    fun saveBoolean(key: String, value: Boolean)

    fun clearUserData(token: String, loginData: String)

    fun logout(): SharedPreferences.Editor
}
