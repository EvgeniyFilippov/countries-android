package com.example.course_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.course_android.R
import com.example.course_android.base.adapter.BaseAdapter
import com.example.domain.dto.model.CapitalItemDto

class AdapterCapitals : BaseAdapter<CapitalItemDto>() {

    class CapitalViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCapital: AppCompatTextView = view.findViewById(R.id.item_capital)
        val cardTvCapital: CardView = view.findViewById(R.id.item_capital_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CapitalViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_capital_layout, parent, false)
        return CapitalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CapitalViewHolder) {
            val item = mDataList[position]
            holder.tvCapital.text = item.capital
            holder.cardTvCapital.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_animation)
        }
    }

}