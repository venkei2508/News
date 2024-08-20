package com.android.news.base

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.news.utils.ThemeManager

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun updateMode() {
        val savedMode = ThemeManager.getSavedTheme(this)
        val changedMode = if (savedMode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(changedMode)
        saveTheme(changedMode)
    }

    fun setUpTheme() {
        val savedMode = ThemeManager.getSavedTheme(this)
        AppCompatDelegate.setDefaultNightMode(savedMode)
    }

    private fun saveTheme(mode: Int) {
        ThemeManager.saveTheme(mode, this)
    }

    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        runOnUiThread {
            Toast.makeText(this, msg, duration).show()
        }
    }
}