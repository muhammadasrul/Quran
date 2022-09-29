package com.acun.quran.util

import android.view.View
import androidx.core.content.ContextCompat
import com.acun.quran.R
import com.google.android.material.snackbar.Snackbar

fun View.hide() {
    visibility = View.GONE
}
fun View.show() {
    visibility = View.VISIBLE
}
fun View.visibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE  else View.GONE
}

fun customToast(view: View, message: String, onClicked: SnackBarOnClickListener) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(ContextCompat.getColor(view.context, R.color.text_black_light))
        setAction("Undo") {
            onClicked.onClicked()
        }
    }.show()
}

interface SnackBarOnClickListener {
    fun onClicked()
}