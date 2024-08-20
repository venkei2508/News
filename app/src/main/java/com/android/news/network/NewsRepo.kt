package com.android.news.network

import android.content.Context
import com.android.news.database.Categories
import com.android.news.database.NewsArticle
import com.android.news.database.NewsDao
import com.android.news.utils.Utilities
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepo @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
    private val context: Context

) {
    suspend fun getTopHeadlines(
        country: String,
        category: String?,
        pageNumber: Int,
        apiKey: String
    ): List<NewsArticle> {
        if (Utilities.isNetworkAvailable(context)) {
            val response = newsApi.getTopHeadlines(country, category, pageNumber, apiKey)
            if (pageNumber == 1) {
                newsDao.deleteAllArticles()
            }
            newsDao.insertArticles(response.news)
        }
        return newsDao.getAllArticles()
    }

    suspend fun fetchSearch(
        language: String,
        query: String?,
        apiKey: String
    ): List<NewsArticle> {
        val response = newsApi.getSearch(language, query, 30, apiKey)
        return response.news
    }

    suspend fun getCategories(
        apiKey: String
    ): List<Categories> {
        if (Utilities.isNetworkAvailable(context)) {
            val response = newsApi.getCategories(apiKey)
            newsDao.deleteAllCategories()
            val lis: ArrayList<Categories> = ArrayList()
            response.categories.forEach {
                val cate = Categories(0, 0, it)
                lis.add(cate)
            }
            newsDao.insertCategories(lis)
        }

        return newsDao.getAllCategories()
    }

    suspend fun getCategories(
    ): List<Categories> {
        return newsDao.getAllCategories()
    }

}