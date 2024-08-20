package com.android.news.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.android.news.R

object ThemeManager {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME = "theme"

    fun saveTheme(mode: Int, context: Context) {
        val sharedPref =context. getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(KEY_THEME, mode)
            apply()
        }
    }


    fun getSavedTheme(context: Context): Int {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) // Default to system theme
    }
}