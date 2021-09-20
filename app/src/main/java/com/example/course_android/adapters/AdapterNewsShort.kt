package com.example.course_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.R
import com.example.course_android.base.adapter.BaseAdapter
import com.example.course_android.utils.loadSvg
import com.example.domain.dto.news.NewsItemDto

class AdapterNewsShort : BaseAdapter<NewsItemDto>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle: AppCompatTextView = view.findViewById(R.id.news_title)
        val tvDescription: AppCompatTextView = view.findViewById(R.id.news_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news_layout, parent, false
            )
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            val item = mDataList[position]
            holder.tvTitle.text = item.title
            holder.tvDescription.text = item.description
            holder.itemView.setOnClickListener { mClickFunction?.invoke(item) }
        }
    }

}