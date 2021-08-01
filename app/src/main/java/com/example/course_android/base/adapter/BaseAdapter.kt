package com.example.course_android.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ItemType> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var mDataList: MutableList<ItemType> = mutableListOf()

    protected var mClickFunction: ((ItemType) -> Unit?)? = null

    fun setItemClick(clickListener: (ItemType) -> Unit) {
        mClickFunction = clickListener
    }

    override fun getItemCount(): Int = mDataList.size

    open fun repopulate(list: MutableList<ItemType>) {
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    open fun addList(list: MutableList<ItemType>) {
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    open fun addItem(item: ItemType) {
        mDataList.add(item)
        notifyItemChanged(mDataList.size - 1)
    }

    open fun clear() {
        mDataList.clear()
        notifyDataSetChanged()
    }

}