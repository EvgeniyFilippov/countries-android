package com.example.course_android.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.course_android.Constants.DEFAULT_COMMENTS
import com.example.course_android.Constants.DEFAULT_LIKES
import com.example.course_android.R
import java.lang.Exception

class CustomLikeBar : ConstraintLayout {

    private var mTvLikes: TextView? = null
    private var mTvComments: TextView? = null
    private var mLikesTextId: Int? = null
    private var mCommentsTextId: Int? = null

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

        layoutParams =
            LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        mTvLikes = view.findViewById(R.id.mLikes)
        mTvComments = view.findViewById(R.id.mComments)
        attrs?.let {
            val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.CustomLikeBar, 0, 0)
            try {
                mLikesTextId =
                    typedArray.getResourceId(R.styleable.CustomLikeBar_customLikebarLikesText, -1)
                mLikesTextId?.let { mTvLikes?.text = DEFAULT_LIKES }
                mCommentsTextId = typedArray.getResourceId(
                    R.styleable.CustomLikeBar_customLikebarCommentsText,
                    -1
                )
                mCommentsTextId?.let { mTvComments?.text = DEFAULT_COMMENTS }
            } catch (e: Exception) {

            } finally {
                typedArray.recycle()
            }
        }
    }


}