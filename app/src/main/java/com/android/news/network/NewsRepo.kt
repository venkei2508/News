package com.android.news.network

import com.android.news.database.NewsArticle
import com.android.news.database.NewsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepo @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) {

    suspend fun getTopHeadlines(
        country: String,
        category: String?,
        query: String?,
        apiKey: String
    ): List<NewsArticle> {
        val response = newsApi.getTopHeadlines(country, category, query, apiKey)
        newsDao.insertNews(response.articles)
        return newsDao.searchNews("%$query%")
    }

}