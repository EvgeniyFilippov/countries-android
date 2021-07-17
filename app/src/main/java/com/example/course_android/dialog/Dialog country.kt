package com.example.course_android.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog

fun Activity.showAlertDialog() {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.setTitle("Alert")
    alertDialog.setMessage("Alert message")
    alertDialog.setButton(
        AlertDialog.BUTTON_NEUTRAL, "OK"
    ) { dialog, which -> dialog.dismiss() }
    alertDialog.show()
}
