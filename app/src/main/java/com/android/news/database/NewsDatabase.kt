package com.android.news.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration


@Database(entities = [NewsArticle::class,Categories::class], version = 3, exportSchema = true , autoMigrations = [
    AutoMigration(from = 1, to = 2),
    AutoMigration(from = 2, to = 3),
])

abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}