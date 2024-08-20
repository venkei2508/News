package com.android.news.frgments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.news.R
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
    private var _binding: FilterBinding? = null
    private val mBinding get() = _binding!!
    private var mList: ArrayList<Categories> = ArrayList()
    private var mSelected: Selected? = null

    private val mAdapter = CommonAdapter<Categories, ItemChipBinding>(
        { inflater, parent, attachToParent -> ItemChipBinding.inflate(inflater, parent, attachToParent) },
        { bind, item, _ ->
            bind.tvTitle.capitalText(item.category)
            val isSelected = item.isSelected

            if (isSelected == 0) {
                bind.cvMain.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_chip_un)
                bind.ivClose.hide()
            } else {
                bind.cvMain.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_chip_selected)
                bind.ivClose.show()
            }
        },
        { _, pos ->
            updateItem(pos)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        vm.fetchCategories()
        return mBinding.root
    }

    private fun setupRecyclerView() {
        mBinding.rvList.apply {
            layoutManager = StaggeredGridLayoutManager(7, RecyclerView.HORIZONTAL)
            adapter = mAdapter
        }
    }

    private fun setupObservers() {
        vm.categories.observe(viewLifecycleOwner) {
            when {
                it != null && it.isNotEmpty() -> {
                    mList = it as ArrayList<Categories>
                    hideLoading()
                    mAdapter.setData(mList)
                }
                else -> showError()
            }
        }
    }

    private fun setupClickListeners() {
        mBinding.btnApply.setOnClickListener { onApply() }
        mBinding.imgClose.setOnClickListener { dismiss() }
    }

    private fun showLoading() {
        with(mBinding) {
            pbLoading.show()
            tvError.gone()
            rvList.gone()
            btnApply.gone()
        }
    }

    private fun showError() {
        with(mBinding) {
            pbLoading.gone()
            tvError.show()
            rvList.gone()
            btnApply.gone()
        }
    }

    private fun hideLoading() {
        with(mBinding) {
            pbLoading.gone()
            tvError.gone()
            rvList.show()
            btnApply.show()
        }
    }

    private fun updateItem(pos: Int) {
        val updatedType = if (mList[pos].isSelected == 0) 1 else 0
        mList[pos].isSelected = updatedType
        mAdapter.notifyItemChanged(pos, mList[pos])
        updateBtn()
    }

    private fun updateBtn() {
        with(mBinding.btnApply) {
            isEnabled = isEnable()
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isEnabled) R.color.purple_200 else R.color.colorGray
                )
            )
        }
    }

    private fun isEnable(): Boolean = mList.any { it.isSelected == 1 }

    private fun onApply() {
        mSelected?.onSelected(ArrayList(mList.filter { it.isSelected == 1 }))
        dismiss()
    }

    fun setSelected(selected: Selected) {
        mSelected = selected
    }

    interface Selected {
        fun onSelected(list: ArrayList<Categories>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}