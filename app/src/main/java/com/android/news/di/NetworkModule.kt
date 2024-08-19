package com.android.news.di

import android.util.Log
import com.android.news.database.NewsDatabase
import com.android.news.network.NewsApi
import com.android.news.network.NewsRepo
import com.android.news.viewmodel.NewsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val TAG ="NetworkModule==>"
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)


    @Provides
    @Singleton
    fun provideNewsRepository(apiService: NewsApi, database: NewsDatabase): NewsRepo {
        Log.e(TAG,"provideNewsRepository created ")
        return NewsRepo(apiService,database.newsDao())
    }


    @Provides
    @Singleton
    fun provideViewModel(res:NewsRepo) : NewsViewModel{
        return NewsViewModel(res)
    }
}