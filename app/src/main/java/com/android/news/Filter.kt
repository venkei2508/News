package com.android.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.news.adapter.CommonAdapter
import com.android.news.database.Categories
import com.android.news.databinding.FilterBinding
import com.android.news.databinding.ItemChipBinding
import com.android.news.utils.capitalText
import com.android.news.utils.gone
import com.android.news.utils.hide
import com.android.news.utils.show
import com.android.news.viewmodel.NewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Filter : BottomSheetDialogFragment() {

    @Inject
    lateinit var vm: NewsViewModel
    private lateinit var mBinding: FilterBinding
    private var mList: ArrayList<Categories> = ArrayList()
    private var mSelected: Selected? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FilterBinding.inflate(inflater, container, false)
        mBinding.rvList.apply {
            layoutManager = StaggeredGridLayoutManager(7, RecyclerView.HORIZONTAL)
            adapter = mAdapter
        }
        showLoading()
        updateBtn()
        mBinding.btnApply.setOnClickListener { onApply() }
        mBinding.imgClose.setOnClickListener { dismiss() }

        vm.categories.observe(this) {
            if (it != null) {
                mList = it as ArrayList<Categories>
                if (mList.isNotEmpty()) {
                    hideLoading()
                    mAdapter.setData(mList)
                } else {
                    showError()
                }
            } else {
                showError()
            }
        }
        vm.fetchCategories()
        return mBinding.root
    }


    private fun showLoading() {
        requireActivity().runOnUiThread {
            mBinding.pbLoading.show()
            mBinding.tvError.gone()
            mBinding.rvList.gone()
            mBinding.btnApply.gone()
        }
    }

    private fun showError() {
        requireActivity().runOnUiThread {
            mBinding.pbLoading.gone()
            mBinding.tvError.show()
            mBinding.rvList.gone()
            mBinding.btnApply.gone()
        }
    }

    private fun hideLoading() {
        requireActivity().runOnUiThread {
            mBinding.pbLoading.gone()
            mBinding.tvError.gone()
            mBinding.rvList.show()
            mBinding.btnApply.show()
        }
    }

    private val mAdapter = CommonAdapter<Categories, ItemChipBinding>(
        { i, ii, iii -> ItemChipBinding.inflate(i, ii, iii) },
        { bind, item, _ ->
            bind.tvTitle.capitalText(item.category)
            val isSelected = item.isSelected

            if (isSelected == 0) {
                bind.cvMain.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_chip_un)
                bind.ivClose.hide()
            } else {
                bind.cvMain.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_chip_selected)
                bind.ivClose.show()
            }
        },
        { _, pos ->
            updateItem(pos)
        }
    )

    private fun updateItem(pos: Int) {
        val updateType = if (mList[pos].isSelected == 0) 1 else 0
        mList[pos].isSelected = updateType
        mList[pos] = mList[pos]
        mAdapter.notifyItemChanged(pos, mList[pos])
        updateBtn()
    }

    private fun updateBtn() {
        if (isEnable()) {
            mBinding.btnApply.isEnabled = true
            mBinding.btnApply.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_200
                )
            )
        } else {
            mBinding.btnApply.isEnabled = false
            mBinding.btnApply.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorGray
                )
            )
        }
    }

    private fun isEnable(): Boolean {
        return mList.any { it.isSelected == 1 }
    }

    private fun onApply() {
        if (mSelected != null) {
            val selected = mList.filter { it.isSelected == 1 } as ArrayList
            mSelected!!.onSelected(selected)
        }
        dismiss()
    }

    fun setSelected(selected: Selected) {
        mSelected = selected
    }

    interface Selected {
        fun onSelected(list: ArrayList<Categories>)
    }

}