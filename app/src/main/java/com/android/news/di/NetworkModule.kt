package com.android.news.di

import android.content.Context
import android.util.Log
import com.android.news.database.NewsDatabase
import com.android.news.network.NewsApi
import com.android.news.network.NewsRepo
import com.android.news.viewmodel.NewsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val TAG ="NetworkModule==>"


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor(5))  // Retry up to 5 times
            .connectTimeout(1, TimeUnit.MINUTES)  // Set connection timeout
            .writeTimeout(1, TimeUnit.MINUTES)    // Set write timeout
            .readTimeout(1, TimeUnit.MINUTES)     // Set read timeout
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.currentsapi.services/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }





    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)


    @Provides
    @Singleton
    fun provideNewsRepository(apiService: NewsApi, database: NewsDatabase,ctx:Context): NewsRepo {
        Log.e(TAG,"provideNewsRepository created ")
        return NewsRepo(apiService,database.newsDao(),ctx)
    }


    @Provides
    @Singleton
    fun provideViewModel(res:NewsRepo) : NewsViewModel{
        return NewsViewModel(res)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}

class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var response: Response
        var lastException: Exception? = null

        while (attempt < maxRetries) {
            try {
                response = chain.proceed(chain.request())
                return response
            } catch (e: Exception) {
                lastException = e
                attempt++
            }
        }
        throw lastException ?: IOException("Unknown network error")
    }
}