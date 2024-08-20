package com.android.news.utils

import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.util.Locale


fun AppCompatTextView.text(msg: String) {
    msg?.let {
        this.text = it
    }
}

fun AppCompatButton.text(msg: String) {
    msg?.let {
        this.text = it
    }
}

fun AppCompatTextView.textColor(color:Int){
    this.setTextColor(ContextCompat.getColor(this.context,color))
}

fun AppCompatTextView.htmlText(str:String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
    }
}

fun AppCompatTextView.capitalText(str: String) {
    str?.let {msg->
        this.text = msg.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

fun String.capitalText(): String {
    return this.lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}


fun AppCompatTextView.color(src:Int){
    this.setTextColor(ContextCompat.getColor(this.context,src))
}


fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

inline fun <reified T : Serializable> Intent.serializeData(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") (getSerializableExtra(key) as T?)
}