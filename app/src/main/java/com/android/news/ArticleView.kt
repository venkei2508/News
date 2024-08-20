package com.android.news

import android.os.Bundle
import com.android.news.database.NewsArticle
import com.android.news.databinding.ActivityArticleViewBinding
import com.android.news.utils.Utilities
import com.android.news.utils.capitalText
import com.android.news.utils.gone
import com.android.news.utils.serializeData
import com.android.news.utils.show
import com.android.news.utils.text

class ArticleView : BaseActivity() {

    private lateinit var mBinding:ActivityArticleViewBinding
    private var mData:NewsArticle?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityArticleViewBinding.inflate(layoutInflater)
        setUpTheme()
        setContentView(mBinding.root)

        mData = intent!!.serializeData("data")
        mBinding.header.imgBack.show()
        mBinding.header.imgSearch.gone()
        mBinding.header.imgFilter.gone()
        mBinding.header.tvHeaderTitle.text("Details")
        mBinding.header.imgBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        mBinding.tvTitle.capitalText(mData!!.title)
        mBinding.tvSubTitle.capitalText(mData!!.description!!)
        mBinding.tvTime.capitalText(Utilities.convertTime(mData!!.published))
        mBinding.header.imgMode.setOnClickListener { updateMode() }
    }
}