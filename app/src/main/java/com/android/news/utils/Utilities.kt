package com.android.news.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

object Utilities {


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            return networkInfo != null && networkInfo.isConnected
        }
    }


    @SuppressLint("NewApi")
    fun convertTime(timeStr: String): String {
        // Define the input format
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")

        // Parse the string to a ZonedDateTime object
        val zonedDateTime = ZonedDateTime.parse(timeStr, inputFormatter)

        // Define the desired output format
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a")

        // Format the ZonedDateTime object to the desired string format
        return zonedDateTime.format(outputFormatter)
    }

}