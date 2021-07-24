package com.example.course_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.R
import com.example.course_android.base.adapter.BaseAdapter
import com.example.course_android.model.allCountries.CountriesDataItem
import com.example.course_android.utils.getLanguageByKey

class AdapterOfAllCountries : BaseAdapter<CountriesDataItem>() {

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCountry: AppCompatTextView = view.findViewById(R.id.item_title)
        val tvCapital: AppCompatTextView = view.findViewById(R.id.item_detail)
        val tvLanguages: AppCompatTextView = view.findViewById(R.id.item_lang)
        val tvArea: AppCompatTextView = view.findViewById(R.id.item_area)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return CountryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            val item = mDataList[position]
            holder.tvCountry.text = item.name
            holder.tvCapital.text = item.capital
            holder.tvLanguages.text = item.languages.getLanguageByKey()
            holder.tvArea.text = item.area.toString()
            holder.itemView.setOnClickListener { mClickFunction?.invoke(item) }
        }
    }

    fun sortAndReplaceItem() {
        mDataList.sortBy { it.area }
        notifyDataSetChanged()
    }

    fun sortDescendingAndReplaceItem() {
        mDataList.sortByDescending { it.area }
        notifyDataSetChanged()
    }

    fun resetSorting() {
        mDataList.sortBy { it.name }
        notifyDataSetChanged()
    }

}