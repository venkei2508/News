package com.android.news.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class CommonAdapter <T, VB : ViewBinding>(
    private val layoutInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val onBind: (VB, T, Int) -> Unit,
    private val onItemClick: ((T, Int) -> Unit)? = null
) : RecyclerView.Adapter<CommonAdapter<T, VB>.ViewHolder>() {

    private var items: ArrayList<T> = ArrayList()

    fun getItem(position: Int): T {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = layoutInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(items[position], position)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBind(holder.binding, items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<T>) {
        for (i in items){
            insert(i)
        }
    }

    private fun insert(i:T){
        items.add(i)
        notifyItemInserted(items.size-1)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}