package com.acun.quranicplus.util

import android.view.View

fun View.toGone() {
    visibility = View.GONE
}
fun View.toVisible() {
    visibility = View.VISIBLE
}
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE  else View.GONE
}