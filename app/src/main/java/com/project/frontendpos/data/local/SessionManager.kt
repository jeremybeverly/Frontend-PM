package com.project.frontendpos.data.local

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "POS_SESSION"
    private const val KEY_TOKEN = "token"

    private lateinit var prefs: SharedPreferences
    var userToken: String? = null
    private set

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        userToken = prefs.getString(KEY_TOKEN, null)
    }

    fun saveToken(token: String) {
        userToken = token
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun isLoggedIn(): Boolean {
        return !userToken.isNullOrBlank()
    }

    fun clearSession(){
        userToken = null
        prefs.edit().clear().apply()
    }

    val formattedToken: String
        get() = "Bearer $userToken"
}