package com.android.news.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * A generic RecyclerView adapter that can be used for various types of lists.
 *
 * @param T The type of the items in the list.
 * @param VB The type of ViewBinding for the list items.
 * @param layoutInflater A function that inflates the layout for each list item.
 * @param onBind A function that binds the data to the ViewBinding for each item.
 * @param onItemClick (Optional) A callback function that gets invoked when an item is clicked.
 */
class CommonAdapter <T, VB : ViewBinding>(
    private val layoutInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val onBind: (VB, T, Int) -> Unit,
    private val onItemClick: ((T, Int) -> Unit)? = null
) : RecyclerView.Adapter<CommonAdapter<T, VB>.ViewHolder>() {

    // List of items to be displayed in the RecyclerView.
    private var items: ArrayList<T> = ArrayList()

    /**
     * Gets the item at the specified position.
     *
     * @param position The position of the item in the list.
     * @return The item at the specified position.
     */
    fun getItem(position: Int): T {
        return items[position]
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a ViewBinding for the item.
     */
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

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBind(holder.binding, items[position], position)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Sets new data for the adapter and refreshes the view.
     *
     * @param items The new data to be set.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    /**
     * Clears all data from the adapter and refreshes the view.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

    /**
     * Adds new data to the existing list and updates the view.
     *
     * @param items The data to be added.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<T>) {
        for (i in items) {
            insert(i)
        }
    }

    /**
     * Inserts a single item to the list and notifies the adapter of the new item.
     *
     * @param i The item to be inserted.
     */
    private fun insert(i: T) {
        items.add(i)
        notifyItemInserted(items.size - 1)
    }

    /**
     * Returns the view type of the item at the specified position.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The view type of the item.
     */
    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
     * A ViewHolder that holds the ViewBinding for each item.
     *
     * @param binding The ViewBinding for the item.
     */
    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

}