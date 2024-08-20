package com.android.news.network

import com.android.news.model.CategoriesResponse
import com.android.news.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("latest-news")
    suspend fun getTopHeadlines(
        @Query("language") language: String,
        @Query("category") category: String?,
        @Query("page_number") page_number: Int?,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("available/categories")
    suspend fun getCategories(
        @Query("apiKey") apiKey: String
    ): CategoriesResponse

    @GET("search")
    suspend fun getSearch(
        @Query("language") language: String,
        @Query("keywords") category: String?,
        @Query("page_number") page_number: Int?,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}