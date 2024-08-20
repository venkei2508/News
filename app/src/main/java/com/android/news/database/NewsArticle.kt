package com.android.news.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "news_articles")
data class NewsArticle(
    @PrimaryKey(autoGenerate = true)
    val news_id: Int = 0,
    val id: String?,
    val title: String,
    val description: String?,
    val author: String?,
    val url: String,
    val image: String?,
    val published: String
) : Serializable

