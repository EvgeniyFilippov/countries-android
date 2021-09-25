package com.example.course_android.base.mvi

interface IViewRenderer<STATE> {
    fun render(state: STATE)
}