package com.example.course_android.utils

interface Transformer<InputType, OutputType> {
    fun transform(item: InputType?): OutputType
}