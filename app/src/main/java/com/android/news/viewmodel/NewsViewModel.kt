package com.android.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.news.database.Categories
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


    private val _categories = MutableLiveData<List<Categories>>()
    val categories: LiveData<List<Categories>> = _categories


    private val _search = MutableLiveData<List<NewsArticle>>()
    val search: LiveData<List<NewsArticle>> = _search

    fun fetchNews(language: String, category: String?, pageNumber: Int, apiKey: String) {
        viewModelScope.launch {
            _articles.value = repository.getTopHeadlines(language, category, pageNumber, apiKey)
        }
    }

    fun fetchSearch(language: String, query: String?,  apiKey: String) {
        viewModelScope.launch {
            _search.value = repository.fetchSearch(language, query, apiKey)
        }
    }

    fun fetchCategories( apiKey: String) {
        viewModelScope.launch {
            _categories.value = repository.getCategories(apiKey)
        }
    }


    fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }
}