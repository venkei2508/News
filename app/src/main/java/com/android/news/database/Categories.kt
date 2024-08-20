package com.android.news.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "categories")
data class Categories(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var isSelected: Int = 0,
    val category:String,
)
