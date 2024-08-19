package com.android.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.news.database.NewsArticle
import com.android.news.network.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel  @Inject constructor(
private val repository: NewsRepo
) : ViewModel() {

    private val _articles = MutableLiveData<List<NewsArticle>>()
    val articles: LiveData<List<NewsArticle>> = _articles

    fun fetchNews(country: String, category: String?, query: String?, apiKey: String) {
        viewModelScope.launch {
            _articles.value = repository.getTopHeadlines(country, category, query, apiKey)
        }
    }

}