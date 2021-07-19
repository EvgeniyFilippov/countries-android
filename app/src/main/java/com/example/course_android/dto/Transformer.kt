package com.example.course_android.dto

interface Transformer<InputType, OutputType> {
    fun transform(item: InputType?): OutputType
}