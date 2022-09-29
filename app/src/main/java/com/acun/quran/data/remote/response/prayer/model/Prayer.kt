package com.acun.quran.data.remote.response.prayer.model

data class Prayer(
    val name: String,
    val time: String
)

fun Prayer.hour(): Int {
    return time.substring(0,2).toInt()
}

fun Prayer.minute(): Int {
    return time.substring(3,5).toInt()
}