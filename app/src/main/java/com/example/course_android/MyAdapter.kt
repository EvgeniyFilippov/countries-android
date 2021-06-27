package com.example.course_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.model.CountriesDataItem
import retrofit2.Callback

class MyAdapter(val context: Callback<List<CountriesDataItem>?>, val countriesList: List<CountriesDataItem>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var titles = arrayOf("First heading", "Second heading", "Third heading", "Fourth heading", "Fifth heading",
        "Sixth heading", "Seventh heading", "Eighth heading", "Ninth heading", "Tenth heading")
    private val details = arrayOf("First description", "Second description", "The third description", "Fourth description",
        "Fifth description", "Sixth description", "Seventh description", "Eighth description", "Ninth description", "Tenth description")
//    private var images = intArrayOf(R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh,
//        R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh)

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
//        holder.itemImage.setImageResource(countriesList[position].flag)
        holder.itemLang.text = countriesList[position].languages.get(0).name
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        var itemImage: AppCompatImageView
        var itemTitle: AppCompatTextView
        var itemDetail: AppCompatTextView
        var itemLang: AppCompatTextView

        init {
//            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            itemLang = itemView.findViewById(R.id.item_lang)
        }
    }
}