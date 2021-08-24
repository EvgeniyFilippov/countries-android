package com.example.course_android.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.R
import com.example.domain.dto.model.CountryDescriptionItemDto

//class MvvmListAdapter : ListAdapter<CountryDescriptionItemDto, MvvmListAdapter.ListViewHolder>(DifferItemCallback()) {
//
//    class DifferItemCallback : DiffUtil.ItemCallback<CountryDescriptionItemDto>() {
//        @SuppressLint("DiffUtilEquals")
//        override fun areContentsTheSame(oldItem: CountryDescriptionItemDto, newItem: CountryDescriptionItemDto): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areItemsTheSame(oldItem: CountryDescriptionItemDto, newItem: CountryDescriptionItemDto): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        return ListViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    inner class ListViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
//        fun bind(item: CountryDescriptionItemDto) {
//            with(containerView) {
//                title.text = item.name
//            }
//        }
//    }
//}