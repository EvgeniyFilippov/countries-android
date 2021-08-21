package com.example.course_android.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.course_android.R

class CustomLikeBar : LinearLayout {

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val view = inflate(context, R.layout.custom_likebar, this)

    }
}