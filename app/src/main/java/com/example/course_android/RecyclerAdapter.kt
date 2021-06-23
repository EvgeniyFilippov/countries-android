package com.example.course_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var titles = arrayOf("First heading", "Second heading", "Third heading", "Fourth heading", "Fifth heading",
        "Sixth heading", "Seventh heading", "Eighth heading", "Ninth heading", "Tenth heading")
    private val details = arrayOf("First description", "Second description", "The third description", "Fourth description",
        "Fifth description", "Sixth description", "Seventh description", "Eighth description", "Ninth description", "Tenth description")
    private var images = intArrayOf(R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh,
        R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh, R.drawable.vinnipuh)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
        holder.itemImage.setImageResource(images[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: AppCompatImageView
        var itemTitle: AppCompatTextView
        var itemDetail: AppCompatTextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
        }
    }
}