package com.android.news

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.news.utils.ThemeManager

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun updateMode() {
        val savedMode = ThemeManager.getSavedTheme(this)
        var changedMode = 0
        changedMode = if (savedMode == AppCompatDelegate.MODE_NIGHT_NO) {
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

    fun showToast(msg:String){
        runOnUiThread {
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        }
    }
}