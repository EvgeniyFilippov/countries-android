package com.example.course_android.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.Constants.DEFAULT_KM
import com.example.course_android.R
import com.example.course_android.base.adapter.BaseAdapter
import com.example.course_android.utils.calculateDistanceFiler
import com.example.course_android.utils.getLanguageByKey
import com.example.course_android.utils.getResultOfCurrentLocation
import com.example.domain.dto.model.CountryDescriptionItemDto

class AdapterOfAllCountries : BaseAdapter<CountryDescriptionItemDto>() {

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCountry: AppCompatTextView = view.findViewById(R.id.item_title)
        val tvCapital: AppCompatTextView = view.findViewById(R.id.item_detail)
        val tvLanguages: AppCompatTextView = view.findViewById(R.id.item_lang)
        val tvArea: AppCompatTextView = view.findViewById(R.id.item_area)
        val tvPopulation: AppCompatTextView = view.findViewById(R.id.like_bar_distance)
        val currentLocationOfUser = getResultOfCurrentLocation()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_country_layout, parent, false)
        return CountryViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            val item = mDataList[position]
            holder.tvCountry.text = item.name
            holder.tvCapital.text = item.capital
            holder.tvLanguages.text = item.languages.getLanguageByKey()
            holder.tvArea.text = item.area.toString()
            holder.tvPopulation.text =
                calculateDistanceFiler(holder.currentLocationOfUser, item).toString() + DEFAULT_KM
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