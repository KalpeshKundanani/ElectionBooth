package com.kalpeshkundanani.electionbooth

import android.content.Context
import android.content.SharedPreferences
import java.security.AccessControlContext

object Cache {
    private const val IS_LOGGED_IN = "IS_LOGGED_IN"
    private const val PREFS_NAME = "ELECTION_BOOTH_PREFS"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getIsLoggedIn(context: Context): Boolean =
        getPrefs(context).getBoolean(IS_LOGGED_IN, false)

    fun setIsLoggedIn(context: Context, loggedIn: Boolean) = getPrefs(context).edit()
        .putBoolean(IS_LOGGED_IN, loggedIn)
        .apply()
}