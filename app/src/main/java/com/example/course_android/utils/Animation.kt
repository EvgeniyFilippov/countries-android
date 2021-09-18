package com.example.course_android.utils

import android.view.View

fun animateButton(button: View, func: () -> Unit) {

    button.animate()?.apply {
        duration = 800
        alpha(.5f)
        scaleXBy(.5f)
        rotationYBy(360f)
        translationYBy(200f)
    }?.withEndAction {
        button.animate()?.apply {
            duration = 800
            alpha(1f)
            scaleXBy(-.5f)
            rotationYBy(360f)
            translationYBy(-200f)
        }
            ?.withEndAction {
                func()
            }
            ?.start()

    }
}