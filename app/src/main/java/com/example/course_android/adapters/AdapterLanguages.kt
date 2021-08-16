package com.example.course_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.R
import com.example.course_android.base.adapter.BaseAdapter
import com.example.domain.dto.model.LanguageOfOneCountryDto

class AdapterLanguages : BaseAdapter<LanguageOfOneCountryDto>() {

    class LanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvLanguages: AppCompatTextView = view.findViewById(R.id.item_lang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_language_layout, parent, false
            )
        return LanguageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LanguageViewHolder) {
            val item = mDataList[position]
            holder.tvLanguages.text = item.name
        }
    }

}