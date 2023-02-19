package com.acun.quranicplus.data.remote.response.prayer.model

data class Prayer(
    val name: String,
    val time: String,
    var isNowPrayer: Boolean,
    var isNearestPrayer: Boolean,
    var isNotificationOn: Boolean = false
)

fun Prayer.hour(): Int {
    return time.substring(0,2).toInt()
}

fun Prayer.minute(): Int {
    return time.substring(3,5).toInt()
}