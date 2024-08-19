package com.android.news

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp : Application() {
    private val TAG ="NewsApp==>"

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG,"called")
    }




}