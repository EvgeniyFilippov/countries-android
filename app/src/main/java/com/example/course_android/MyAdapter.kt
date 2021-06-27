package com.example.course_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.model.CountriesDataItem
import kotlinx.coroutines.joinAll
import retrofit2.Callback

class MyAdapter(val context: Callback<List<CountriesDataItem>?>, private val countriesList: List<CountriesDataItem>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return countriesList.size
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = countriesList[position].name
        holder.itemDetail.text = countriesList[position].capital
        val myStringBuilder = StringBuilder()
        for (n in countriesList[position].languages.indices) {
            myStringBuilder.append(countriesList[position].languages.get(n).name)
            if (n < countriesList[position].languages.size - 1) {
                myStringBuilder.append(", ")
            }
        }
        holder.itemLang.text = myStringBuilder
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemTitle: AppCompatTextView
        var itemDetail: AppCompatTextView
        var itemLang: AppCompatTextView

        init {
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            itemLang = itemView.findViewById(R.id.item_lang)
        }
    }
}