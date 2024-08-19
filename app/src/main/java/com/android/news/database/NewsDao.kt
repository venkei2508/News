package com.android.news.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(articles: List<NewsArticle>)

    @Query("SELECT * FROM news_articles WHERE title LIKE :query")
    suspend fun searchNews(query: String): List<NewsArticle>
}