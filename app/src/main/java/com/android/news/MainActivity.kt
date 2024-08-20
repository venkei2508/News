package com.android.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.news.adapter.CommonAdapter
import com.android.news.base.BaseActivity
import com.android.news.database.Categories
import com.android.news.database.NewsArticle
import com.android.news.databinding.ActivityMainBinding
import com.android.news.databinding.ItemArticlesBinding
import com.android.news.databinding.ItemChipBinding
import com.android.news.frgments.Filter
import com.android.news.utils.Constants
import com.android.news.utils.Utilities
import com.android.news.utils.capitalText
import com.android.news.utils.gone
import com.android.news.utils.show
import com.android.news.utils.text
import com.android.news.viewmodel.NewsViewModel
import com.android.news.views.ArticleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity(), Filter.Selected {
    private lateinit var binding: ActivityMainBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private val filterList = ArrayList<Categories>()
    private var offset = 1
    private lateinit var layoutManager: LinearLayoutManager
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setUpTheme()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initUI()
        observeViewModel()
        handleSearch()
    }

    private fun initUI() {
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvList.apply {
            layoutManager = this@MainActivity.layoutManager
            adapter = newsAdapter
        }

        binding.rvFilter.adapter = filterAdapter
        showLoading()
        fetchInitialData()
    }

    private fun fetchInitialData() {
        if (Utilities.isNetworkAvailable(this)) {
            newsViewModel.fetchNews("en", null, offset, Constants.API_KEY)
            newsViewModel.fetchCategories(Constants.API_KEY)
        } else {
            showToast(Constants.NO_INTERNET)
            showError()
        }
    }

    private fun observeViewModel() {
        newsViewModel.articles.observe(this) { articles ->
            hideLoading()
            if (articles.isNullOrEmpty()) {
                showError()
            } else {
                Timber.d("Articles size: ${articles.size}")
                newsAdapter.setData(articles as ArrayList<NewsArticle>)
            }
        }

        newsViewModel.search.observe(this) { articles ->
            hideLoader()
            if (articles.isNullOrEmpty()) {
                showToast("No Results Found...!")
            } else {
                Timber.d("Search Articles size: ${articles.size}")
                newsAdapter.setData(articles as ArrayList<NewsArticle>)
            }
        }
    }

    private fun handleSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchNews(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel() // Cancel the previous job if it's still running
                searchJob = lifecycleScope.launch {
                    delay(300) // Debounce time in milliseconds
                    newText?.let {
                        if (it.isEmpty()) {
                            fetchInitialData()
                        } else {
                            searchNews(it)
                        }
                    }
                }
                return true
            }
        })
    }

    private fun searchNews(query: String) {
        if (Utilities.isNetworkAvailable(this)) {
            showLoader()
            newsViewModel.fetchSearch("en", query, Constants.API_KEY)
        } else {
            showToast(Constants.NO_INTERNET)
        }
    }

    private fun showLoading() = with(binding) {
        pbLoading.show()
        tvError.gone()
        rvList.gone()
    }

    private fun hideLoading() = with(binding) {
        pbLoading.gone()
        tvError.gone()
        rvList.show()
    }

    private fun showLoader() = binding.pbLoading.show()

    private fun hideLoader() = binding.pbLoading.gone()

    private fun showError() = with(binding) {
        if (offset == 1) {
            pbLoading.gone()
            tvError.show()
            rvList.gone()
        }
    }

    private val newsAdapter = CommonAdapter<NewsArticle, ItemArticlesBinding>(
        { inflater, parent, attach -> ItemArticlesBinding.inflate(inflater, parent, attach) },
        { bind, item, _ ->
            bind.tvTitle.capitalText(item.title)
            bind.tvSubTitle.capitalText(item.description ?: "")
            bind.tvTime.text(Utilities.convertTime(item.published))
        },
        { item, _ ->
            val intent = Intent(this, ArticleView::class.java)
            intent.putExtra("data", item)
            startActivity(intent)
        }
    )

    override fun onSelected(list: ArrayList<Categories>) {
        if (Utilities.isNetworkAvailable(this)) {
            filterList.clear()
            filterList.addAll(list)
            filterAdapter.setData(filterList)
            updateFilters()
        } else {
            showToast(Constants.NO_INTERNET)
        }
    }

    private fun updateFilters() {
        val categories = filterList.joinToString(",") { it.category }
        newsViewModel.fetchNews("en", if (filterList.isEmpty()) null else categories, 1, Constants.API_KEY)
        if (filterList.isEmpty()) {
            binding.rvFilter.gone()
        } else {
            binding.rvFilter.show()
        }
    }

    private val filterAdapter = CommonAdapter<Categories, ItemChipBinding>(
        { inflater, parent, attach -> ItemChipBinding.inflate(inflater, parent, attach) },
        { bind, item, _ -> bind.tvTitle.capitalText(item.category) },
        { _, pos -> removeFilter(pos) }
    )

    private fun removeFilter(pos: Int) {
        filterList.removeAt(pos)
        filterAdapter.notifyItemRemoved(pos)
        updateFilters()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)

        searchItem?.setOnMenuItemClickListener {
            toggleSearchBarVisibility()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                toggleSearchBarVisibility()
                true
            }
            R.id.action_filter -> {
                Filter().apply {
                    show(supportFragmentManager, "Filter")
                    setSelected(this@MainActivity)
                }
                true
            }
            R.id.action_theme -> {
                updateMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleSearchBarVisibility() {
        binding.searchView.visibility =
            if (binding.searchView.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}
