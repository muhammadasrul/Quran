package com.acun.quranicplus.util

import android.view.View
import androidx.core.content.ContextCompat
import com.acun.quranicplus.R
import com.google.android.material.snackbar.Snackbar

fun View.toGone() {
    visibility = View.GONE
}
fun View.toVisible() {
    visibility = View.VISIBLE
}
fun View.setVisibility(isVisible: Boolean) {
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