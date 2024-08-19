package com.android.news.network

import com.android.news.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String?,
        @Query("q") query: String?,
        @Query("apiKey") apiKey: String
    ): NewsResponse

}