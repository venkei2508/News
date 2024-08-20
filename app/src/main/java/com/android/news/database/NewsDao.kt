package com.android.news.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticle>)

    @Query("SELECT * FROM news_articles")
    suspend fun getAllArticles(): List<NewsArticle>

    @Query("SELECT * FROM news_articles WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    suspend fun searchArticles(searchQuery: String): List<NewsArticle>

    @Query("DELETE FROM news_articles")
    suspend fun deleteAllArticles()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(articles: List<Categories>)


    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Categories>

}