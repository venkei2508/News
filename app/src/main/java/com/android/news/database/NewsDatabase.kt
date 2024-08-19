package com.android.news.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NewsArticle::class], version = 1, exportSchema = true)

abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}