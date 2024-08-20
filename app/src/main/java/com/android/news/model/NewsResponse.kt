package com.android.news.model

import com.android.news.database.NewsArticle

class NewsResponse {

    var news:List<NewsArticle> = ArrayList()
}

data class CategoriesResponse(
    var status:String,
    var categories:ArrayList<String> = ArrayList()
)