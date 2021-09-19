package com.example.course_android.utils

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView

fun animateButton(button: View, func: () -> Unit) {

    button.animate()?.apply {
        duration = 600
        alpha(.5f)
        scaleXBy(.5f)
//        rotationYBy(360f)
        translationYBy(200f)
    }?.withEndAction {
        button.animate()?.apply {
            duration = 600
            alpha(1f)
            scaleXBy(-.5f)
//            rotationYBy(360f)
            translationYBy(-200f)
        }
            ?.withEndAction {
                func()
            }
            ?.start()
    }
}

fun animateMainPicture(listPartOfPicture: List<AppCompatImageView?>, listButtons: List<AppCompatButton?>) {

    listPartOfPicture[0]?.alpha = .0f
    listPartOfPicture[0]?.translationY = -300f
    listPartOfPicture[1]?.alpha = .0f
    listPartOfPicture[1]?.translationY = -300f
    listPartOfPicture[2]?.alpha = .0f
    listPartOfPicture[2]?.translationY = 300f
    listPartOfPicture[3]?.alpha = .0f
    listPartOfPicture[3]?.translationY = 300f

    listPartOfPicture[0]?.animate()
        ?.setDuration(1500)
        ?.translationYBy(300f)
        ?.alpha(1f)
        ?.withEndAction {
            listPartOfPicture[1]?.animate()
                ?.setDuration(1500)
                ?.translationYBy(300f)
                ?.alpha(1f)
                ?.withEndAction {
                    listPartOfPicture[2]?.animate()
                        ?.setDuration(1500)
                        ?.translationYBy(-300f)
                        ?.alpha(1f)
                        ?.withEndAction {
                            listPartOfPicture[3]?.animate()
                                ?.setDuration(1500)
                                ?.translationYBy(-300f)
                                ?.alpha(1f)
                                ?.withEndAction {
                                    makeVisibility(listButtons, true)
                                }
                        }

                }
        }
        ?.start()
}

fun makeVisibility(list: List<View?>, visible: Boolean) {

    if (visible) {
        list.forEach {
            it?.visibility = View.VISIBLE
        }
    } else {
        list.forEach {
            it?.visibility = View.GONE
        }
    }

}